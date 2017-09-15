package pl.clarin.chronopress.presentation.page.quantity;

import com.vaadin.cdi.CDIView;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
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
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateTimeSerieEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.SentenceQuantitativeAnalysisTab;
import pl.clarin.chronopress.presentation.page.dataanalyse.WordQuantitativeAnalysisTab;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(QuantityView.ID)
public class QuantityViewImpl extends AbstractView<QuantityViewPresenter> implements QuantityView {

    @Inject
    private Instance<QuantityViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    @Inject
    WordQuantitativeAnalysisTab wordQuantitativeAnalysisTab;

    @Inject
    SentenceQuantitativeAnalysisTab sentenceQuantitativeAnalysisTab;

    @Inject
    javax.enterprise.event.Event<CalculateTimeSerieEvent> calculateTimeSeries;

    private VerticalLayout loading;

    private HorizontalLayout buttonWrapper;

    private final Map<String, CalculationResult> results = new HashMap<>();

    CalculationResult calculation;

    private boolean filterVisible = false;

    private VerticalLayout layout;

    private TabSheet sheetLeft;
    private TabSheet sheetRight;

    @PostConstruct
    public void init() {
        selectionForm.setVisible(false);

        sheetLeft = new TabSheet(wordQuantitativeAnalysisTab);
        sheetRight = new TabSheet(sentenceQuantitativeAnalysisTab);

        final Label txt1 = new Label();

        String t = "<span>Moduł ten pozwala na automatyczne obliczanie podstawowych statystyk skupienia i rozrzutu cech dla tekstów.</span>";

        txt1.setValue(VaadinUI.infoMessage(t));
        txt1.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt1);

        final Label txt2 = new Label();
        String t1 = "<span>System rozpoznaje wyrazy tekstowe, definiowane jako ciagi znaków między spacjami.</span></br>"
                + "<span>Jednostką pomiaru jest litera. System nie rozpoznaje jednostek wielowyrazowych typu \"na co dzień \" lub \"wilk morski\"</span>";

        txt2.setValue(VaadinUI.infoMessage(t1));
        txt2.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(txt2);

        final Label txt3 = new Label();
        String t2 = "<span>System rozpoznaje zdania zgodnie z interpunkcją w tekście.</br> Jednostką pomiaru są litery lub pojedyncze wyrazy.</span>";

        txt3.setValue(VaadinUI.infoMessage(t2));
        txt3.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent3 = new VerticalLayout();
        popupContent3.addComponent(txt3);

        final PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);
        final PopupView help1 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);
        final PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent3);

        Label desc = new Label("Analiza ilościowa");
        desc.addStyleName("press-text-large");

        Button filter = new MButton("Filtr danych")
                .withStyleName(ValoTheme.BUTTON_TINY, ValoTheme.BUTTON_LINK)
                .withListener(l -> {
                    filterVisible = !filterVisible;
                    selectionForm.setVisible(filterVisible);
                });

        Button executeWord = new MButton("Generuj statystyki wyrazów")
                .withListener(l -> {
                    getPresenter().onCalculateWordQuantitive(selectionForm.getData(), wordQuantitativeAnalysisTab.getWordAnalysisDTO());
                })
                .withStyleName(ValoTheme.BUTTON_SMALL);

        Button executeSentence = new MButton("Generuj statystyki zdań")
                .withListener(l -> {
                    getPresenter().onCalculateSentenceQuantitive(selectionForm.getData(), sentenceQuantitativeAnalysisTab.getSentenceAnalysisDTO());
                })
                .withStyleName(ValoTheme.BUTTON_SMALL);

        HorizontalLayout sheet = new MHorizontalLayout(sheetLeft, sheetRight);

        VerticalLayout content = new MVerticalLayout()
                .with(new MHorizontalLayout(desc, help).withSpacing(true), filter, selectionForm, sheet,
                        new MHorizontalLayout(executeWord, executeSentence)
                        .withSpacing(true))
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

    @Override
    protected QuantityViewPresenter generatePresenter() {
        return presenter.get();
    }

    public void setInitDataSelection(InitDataSelectionDTO data) {
        selectionForm.setAuthors(data.getAuthors());
        selectionForm.setYears(data.getYears());
        selectionForm.setExposition(data.getExpositions());
        selectionForm.setPeriods(data.getPeriods());
        selectionForm.setTiles(data.getJournalTitles());
        selectionForm.setAudience(data.getAudience());
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
