package pl.clarin.chronopress.presentation.page.start;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.*;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.page.concordance.ConcordanceView;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.frequency.FrequencyView;
import pl.clarin.chronopress.presentation.page.mapnames.MapnamesView;
import pl.clarin.chronopress.presentation.page.profile.ProfilesView;
import pl.clarin.chronopress.presentation.page.quantity.QuantityView;
import pl.clarin.chronopress.presentation.page.timeseries.TimeSeriesView;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(StartView.ID)
public class StartViewImpl extends AbstractView<StartViewPresenter> implements StartView {

    @Inject
    private Instance<StartViewPresenter> presenter;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    final TextField search = new TextField();

    private boolean filterVisible = false;

    private VerticalLayout content;

    CalculationResult calculation;

    private Panel linksPanel;

    private final Map<String, CalculationResult> results = new HashMap<>();

    private Button timeSerriesBtn, profilesBtn, concordanceBtn, gmapBtn, freqBtn, quanAnalBtn;
    private Label searchInCorpora, searchInfo, portalOptions;

    @PostConstruct
    public void init() {
        search.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth("60%");
        search.setImmediate(true);

        selectionForm.setVisible(false);
        searchInfo = new Label();
        portalOptions = new Label();
        portalOptions.addStyleName("press-text-align");

        HorizontalLayout opis = new MHorizontalLayout()
                .withWidth("60%")
                .with(portalOptions)
                .withAlign(portalOptions, Alignment.MIDDLE_CENTER);

        searchInfo.setContentMode(ContentMode.HTML);

        VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(searchInfo);

        // The component itself
        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);
        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        searchInCorpora = new MLabel().withStyleName("press-text-large");

        HorizontalLayout desc = new MHorizontalLayout()
                .with(new MHorizontalLayout(searchInCorpora, help2)
                        .withWidth("-1")
                        .withSpacing(true))
                .withWidth("60%");

        VerticalLayout searchContent = new MVerticalLayout()
                .with(desc, search, selectionForm, opis)
                .withAlign(desc, Alignment.MIDDLE_CENTER)
                .withAlign(search, Alignment.MIDDLE_CENTER)
                .withAlign(opis, Alignment.MIDDLE_CENTER)
                .withMargin(true)
                .withFullWidth();

        search.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                getPresenter().onSearch(search.getValue());
            }
        });

        timeSerriesBtn = new MButton()
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(TimeSeriesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

        profilesBtn = new MButton()
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(ProfilesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

        concordanceBtn = new MButton()
                 .withListener(l -> {
                    navigation.fire(new NavigationEvent(ConcordanceView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

        gmapBtn = new MButton()
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(MapnamesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

        freqBtn = new MButton()
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(FrequencyView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

        quanAnalBtn = new MButton()
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(QuantityView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE)
                .withStyleName("ch-responsive-btn");

       CssLayout gridLayout = new MCssLayout()
                .withWidth(70, Unit.PERCENTAGE);
        gridLayout.addComponent(timeSerriesBtn);
        gridLayout.addComponent(profilesBtn);
        gridLayout.addComponent(concordanceBtn);
        gridLayout.addComponent(gmapBtn);
        gridLayout.addComponent(freqBtn);
        gridLayout.addComponent(quanAnalBtn);

        Panel searchPanel = new MPanel(searchContent)
                .withStyleName(ValoTheme.PANEL_BORDERLESS)
                .withFullWidth();

        linksPanel = new MPanel(new MHorizontalLayout(gridLayout)
                .withAlign(gridLayout, Alignment.MIDDLE_CENTER)
                .withFullWidth()
        ).withFullWidth()
                .withStyleName(ValoTheme.PANEL_BORDERLESS);

        content = new MVerticalLayout(linksPanel)
                .withSize(MSize.FULL_SIZE);

        VerticalLayout layout = new MVerticalLayout()
                .with(searchPanel, content)
                .withMargin(true)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(layout);
        setSizeFull();
    }

    @Override
    protected StartViewPresenter generatePresenter() {
        return presenter.get();
    }

    @Override
    public void addResultPanel(CalculationResult result) {
        getUI().access(() -> {
            setCalculation(result);
            show();
        });
    }

    public void show() {
        Component panel = calculation.showResult();
        content.removeAllComponents();
        content.addComponent(panel);
    }

    @Override
    public void setInitDataSelection(InitDataSelectionDTO data) {
        selectionForm.setAuthors(data.getAuthors());
        selectionForm.setYears(data.getYears());
        selectionForm.setExposition(data.getExpositions());
        selectionForm.setPeriods(data.getPeriods());
        selectionForm.setTiles(data.getJournalTitles());
        selectionForm.setAudience(data.getAudience());
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    @Override
    public void onViewEnter() {
        content.removeAllComponents();
        content.addComponent(linksPanel);
        initLabels();
    }

    private void initLabels() {
        searchInfo.setValue(VaadinUI.infoMessage(provider.getProperty("start.view.search.info")));
        portalOptions.setValue(provider.getProperty("start.view.portal.options"));
        searchInCorpora.setValue(provider.getProperty("start.view.search.in.corpora"));
        timeSerriesBtn.setCaption(provider.getProperty("start.view.tile.time.series"));
        profilesBtn.setCaption(provider.getProperty("start.view.tile.profiles"));
        concordanceBtn.setCaption(provider.getProperty("start.view.tile.concordance"));
        gmapBtn.setCaption(provider.getProperty("start.view.tile.gmap"));
        freqBtn.setCaption(provider.getProperty("start.view.tile.frequency"));
        quanAnalBtn.setCaption(provider.getProperty("start.view.tile.quan.anal"));

    }

}
