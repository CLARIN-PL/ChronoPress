package pl.clarin.chronopress.presentation.page.profile;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.WordQuantitativeAnalysisTab;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.DataExplorationDTO;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(ProfilesView.ID)
public class ProfilesViewImpl extends AbstractView<ProfilesViewPresenter> implements ProfilesView {

    @Inject
    private Instance<ProfilesViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    @Inject
    DataExplorationForm dataExplorationForm;

    private VerticalLayout loading;

    private HorizontalLayout buttonWrapper;

    private final Map<String, CalculationResult> results = new HashMap<>();

    CalculationResult calculation;

    private boolean filterVisible = false;

    private VerticalLayout layout;

    public DataExplorationDTO getDataExplorationDTO() {
        try {
            return dataExplorationForm.getDataExplorationDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(WordQuantitativeAnalysisTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        dataExplorationForm.reset();
    }

    @PostConstruct
    public void init() {
        selectionForm.setVisible(false);
        dataExplorationForm.selectOptionType(DataExplorationForm.DataExplorationType.PROFILE);

        Label desc = new Label(provider.getProperty("view.profile.title"));
        desc.addStyleName("press-text-large");

        Label txt = new Label(VaadinUI.infoMessage(provider.getProperty("view.profile.desc")));

        dataExplorationForm.setLemmaHelp(VaadinUI.infoMessage(provider.getProperty("view.profile.help")));
        txt.setContentMode(ContentMode.HTML);
        VerticalLayout popupContent = new VerticalLayout();

        popupContent.addComponent(txt);

        // The component itself
        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        Button filter = new MButton(provider.getProperty("label.filter"))
                .withStyleName(ValoTheme.BUTTON_TINY, ValoTheme.BUTTON_LINK)
                .withListener(l -> {
                    filterVisible = !filterVisible;
                    selectionForm.setVisible(filterVisible);
                });

        Button execute = new MButton(provider.getProperty("view.profile.btn.generate"))
                .withListener(l -> {
                    presenter.get().onCalculateDataExploration(new CalculateDataExplorationEvent(selectionForm.getData(), getDataExplorationDTO()));
                })
                .withStyleName(ValoTheme.BUTTON_SMALL);

        VerticalLayout content = new MVerticalLayout()
                .with(new MHorizontalLayout(desc, help)
                        .withSpacing(true),
                        filter, selectionForm, dataExplorationForm, execute)
                .withStyleName(ChronoTheme.START_PANEL)
                .withMargin(true)
                .withWidth("-1px");

        layout = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(true)
                .withFullWidth()
                .with(content)
                .withStyleName("press-margin-top")
                .withAlign(content, Alignment.MIDDLE_CENTER);

        setCompositionRoot(layout);
        setSizeFull();
    }

    private VerticalLayout initializeLoading() {

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);

        HorizontalLayout loadingNotification = new MHorizontalLayout()
                .withSpacing(true)
                .with(progressBar, new Label(provider.getProperty("label.loading")));

        return new MVerticalLayout()
                .withMargin(true)
                .with(loadingNotification)
                .withAlign(loadingNotification, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void addResultPanel(CalculationResult result) {
        getUI().access(() -> {
            setCalculation(result);
            show();
        });
    }

    public void show() {
        Component panel = createContentWrapper(calculation.showResult(), calculation.getType());
        layout.addComponent(panel);
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    public void setInitDataSelection(InitDataSelectionDTO data) {
        selectionForm.setAuthors(data.getAuthors());
        selectionForm.setYears(data.getYears());
        selectionForm.setExposition(data.getExpositions());
        selectionForm.setPeriods(data.getPeriods());
        selectionForm.setTiles(data.getJournalTitles());
        selectionForm.setAudience(data.getAudience());
    }

    @Override
    protected ProfilesViewPresenter generatePresenter() {
        return presenter.get();
    }

    private Component createContentWrapper(final Component content, String type) {

        content.setCaption(null);
        content.addStyleName(ChronoTheme.PANEL_CONTENT);
        Label caption = new MLabel(content.getCaption())
                .withStyleName(ValoTheme.LABEL_H4)
                .withStyleName(ValoTheme.LABEL_COLORED)
                .withStyleName(ValoTheme.LABEL_NO_MARGIN);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        HorizontalLayout toolbar = new MHorizontalLayout()
                .withStyleName(ChronoTheme.PANEL_TOOLBAR)
                .withFullWidth()
                .with(caption, tools)
                .withExpand(caption, 1)
                .withAlign(caption, Alignment.MIDDLE_LEFT);

        CssLayout card = new MCssLayout(toolbar, content)
                .withFullWidth()
                .withStyleName(ValoTheme.LAYOUT_CARD);

        final CssLayout slot = new MCssLayout(card)
                .withFullWidth();
        slot.setId(type);

        MenuBar.MenuItem root = tools.addItem("", FontAwesome.TIMES_CIRCLE, (MenuBar.Command) selectedItem -> {
            results.remove(slot.getId());
            layout.removeComponent(slot);
        });
        return slot;
    }
}
