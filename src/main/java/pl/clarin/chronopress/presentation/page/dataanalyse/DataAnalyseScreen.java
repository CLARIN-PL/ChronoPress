package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class DataAnalyseScreen extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    @Inject
    WordQuantitativeAnalysisTab wordQuantitativeAnalysisTab;

    @Inject
    SentenceQuantitativeAnalysisTab sentenceQuantitativeAnalysisTab;

    @Inject
    TimeSeriesTab timeSeriesTab;

    @Inject
    DataExplorationTab dataExplorationTab;

    @Inject
    javax.enterprise.event.Event<CalculateTimeSerieEvent> calculateTimeSeries;

    @Inject
    javax.enterprise.event.Event<CalculateWordQuantitiveAnalysisEvent> calculateWordQuanEvent;

    @Inject
    javax.enterprise.event.Event<CalculateSentenceQuantitiveAnalysisEvent> calculateSentenceQuanEvent;

    @Inject
    javax.enterprise.event.Event<CalculateDataExplorationEvent> calculateDataExploration;

    private final Map<String, CalculationResult> results = new HashMap<>();

    CalculationResult calculation;

    @Inject
    javax.enterprise.event.Event<ShowAnalyseScreenEvent> showScreen;

    private VerticalLayout loading;

    private HorizontalLayout buttonWrapper;
    private VerticalLayout layout;
    private TabSheet sheet;
    private Button execute;

    @PostConstruct
    public void init() {

        loading = initializeLoading();

        sheet = new TabSheet(
                wordQuantitativeAnalysisTab,
                sentenceQuantitativeAnalysisTab,
                timeSeriesTab, dataExplorationTab);

        sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        sheet.setWidth(80, Unit.PERCENTAGE);

        execute = new MButton()
                .withCaption(provider.getProperty("button.execute"))
                .withIcon(FontAwesome.HAND_O_RIGHT)
                .withStyleName(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_SMALL)
                .withListener(l -> {
                    if (sheet.getSelectedTab() instanceof WordQuantitativeAnalysisTab) {
                        WordAnalysisDTO dto = wordQuantitativeAnalysisTab.getWordAnalysisDTO();
                        if (dto.getWordAveragesLengthHistogram() || dto.getWordZipfHistogram()) {
                            calculateWordQuanEvent.fire(new CalculateWordQuantitiveAnalysisEvent(
                                    selectionForm.getData(), dto));
                        }
                    }
                    if (sheet.getSelectedTab() instanceof SentenceQuantitativeAnalysisTab) {
                        SentenceAnalysisDTO dto = sentenceQuantitativeAnalysisTab.getSentenceAnalysisDTO();
                        if (dto.getSentenceAverageLengthHistogram()) {
                            calculateSentenceQuanEvent.fire(new CalculateSentenceQuantitiveAnalysisEvent(
                                    selectionForm.getData(), dto));
                        }
                    }
                    if (sheet.getSelectedTab() instanceof TimeSeriesTab) {
                        if (timeSeriesTab.getTimeSeriesDTO().getTimeSeriesCalculation()) {
                            calculateTimeSeries.fire(new CalculateTimeSerieEvent(selectionForm.getData(),
                                    timeSeriesTab.getTimeSeriesDTO()));
                        }
                    }
                    if (sheet.getSelectedTab() instanceof DataExplorationTab) {
                        calculateDataExploration.fire(new CalculateDataExplorationEvent(selectionForm.getData(),
                                dataExplorationTab.getDataExplorationDTO()));
                    }
                });

        Button reset = new MButton()
                .withCaption("Reset")
                .withIcon(FontAwesome.TIMES_CIRCLE)
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withListener(l -> resetAll());

        HorizontalLayout buttons = new MHorizontalLayout(execute, reset)
                .withSpacing(true);

        buttonWrapper = new MHorizontalLayout()
                .withWidth("85%")
                .withMargin(new MarginInfo(false, true, true, true))
                .with(buttons)
                .withAlign(buttons, Alignment.TOP_RIGHT);

        HorizontalLayout wrapper = new MHorizontalLayout()
                .withFullWidth()
                .with(selectionForm)
                .withAlign(selectionForm, Alignment.TOP_CENTER);

        HorizontalLayout sheetWrapper = new MHorizontalLayout()
                .withFullWidth()
                .withMargin(new MMarginInfo(false, true))
                .with(sheet)
                .withAlign(sheet, Alignment.TOP_CENTER);

        layout = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(false)
                .withFullWidth()
                .with(new Title(FontAwesome.COGS, provider.getProperty("view.data.analyse.title")),
                        wrapper, sheetWrapper, buttonWrapper);

        setCompositionRoot(layout);
    }

    public void selectConcordance(String txt) {
        sheet.setSelectedTab(dataExplorationTab);
        dataExplorationTab.setConcordanceLemma(txt);
        execute.click();
    }

    private void resetAll() {
        selectionForm.reset();
        timeSeriesTab.reset();
        dataExplorationTab.reset();
        sentenceQuantitativeAnalysisTab.reset();
        wordQuantitativeAnalysisTab.reset();
    }

    public void setInitDataSelection(InitDataSelectionDTO data) {
        selectionForm.setAuthors(data.getAuthors());
        selectionForm.setYears(data.getYears());
        selectionForm.setExposition(data.getExpositions());
        selectionForm.setPeriods(data.getPeriods());
        selectionForm.setTiles(data.getJournalTitles());
        selectionForm.setAudience(data.getAudience());
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

    public void showLoadingIndicator() {
        layout.replaceComponent(buttonWrapper, loading);
    }

    public void hideLoadingIndicator() {
        layout.replaceComponent(loading, buttonWrapper);
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    public Boolean hasResult(String str) {
        return results.containsKey(str);
    }

    public CalculationResult getResult(String type) {
        return results.get(type);
    }

    public void show() {
        Component panel = createContentWrapper(calculation.showResult(), calculation.getType());
        layout.addComponent(panel);
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
