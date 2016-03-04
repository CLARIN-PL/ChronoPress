package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.dto.*;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.infrastructure.components.results.*;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringView(name = DataAnalysisView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class DataAnalysisView extends DefaultView<DataAnalysisPresenter> implements View {

	public static final String VIEW_NAME = "analysis";
	private final TabSheet sheet = new TabSheet();

	@Autowired
	private DbPropertiesProvider provider;
	@Autowired
	private DataSelectionPanel dataSelectionPanel;
	@Autowired
	private QuantitativeAnalysisTab quantitativeAnalysisTab;
	@Autowired
	private TimeSeriesTab timeSeriesTab;
	@Autowired
	private DataExplorationTab dataExplorationTab;

	@Autowired
	private Result result;

	@Autowired
	public DataAnalysisView(DataAnalysisPresenter presenter) {
		super(presenter);
	}

	@PostConstruct
	public void init() {
		addComponent(new Title(FontAwesome.COGS, provider.getProperty("view.data.analyse.title")));
		setSpacing(true);
		setWidth(100, Unit.PERCENTAGE);
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);

		layout.setSpacing(true);
		layout.addComponent(dataSelectionPanel);
		layout.setComponentAlignment(dataSelectionPanel, Alignment.TOP_LEFT);

		sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		layout.addComponent(sheet);
		layout.setExpandRatio(dataSelectionPanel, 1f);
		layout.setExpandRatio(sheet, 2.5f);

		addComponent(layout);
		addComponent(result);

		initDataSelectionTab();
		initQuantitativeAnalysisTab();
		initTimeSeriesTab();
		initDataExploration();
	}


	private void initDataSelectionTab(){
		dataSelectionPanel.getYears().addItems(presenter.loadYears());
		dataSelectionPanel.getTitles().addItems(presenter.loadTitles());
		dataSelectionPanel.getExpositions().addItems(presenter.loadExpositions());
		dataSelectionPanel.getPeriods().addItems(presenter.loadPeriods());
		dataSelectionPanel.getSearchAuthorsPanel().populateContainer(presenter.loadAuthors());
	}

	private void initQuantitativeAnalysisTab(){
		sheet.addComponent(quantitativeAnalysisTab);
		quantitativeAnalysisTab.getAcceptButton().addClickListener(event ->
				presenter.executeQuantitativeCalculations());

		quantitativeAnalysisTab.getClearButton().addClickListener(event -> {
			dataSelectionPanel.reset();
			quantitativeAnalysisTab.reset();
		});

		timeSeriesTab.getAcceptButton().addClickListener(e -> presenter.executeTimeSeriesCalculations());
		timeSeriesTab.getClearButton().addClickListener(e -> {
			dataSelectionPanel.reset();
			timeSeriesTab.reset();
		});

		dataExplorationTab.getAcceptButton().addClickListener(e -> presenter.executeDataExplorationCalculations());
		dataExplorationTab.getClearButton().addClickListener(e -> {
			dataSelectionPanel.reset();
			dataExplorationTab.reset();
		});
	}

	private void initTimeSeriesTab() {
		sheet.addComponent(timeSeriesTab);
	}

	private void initDataExploration() {
		sheet.addComponent(dataExplorationTab);
	}

	public void showQuantitativeAnalysisResult(QuantitativeAnalysisResult result){
		getUI().access(() -> {
			if (result.isWordAverageCalculations()) {
				WordAverageLengthHistogram histogram =
						new WordAverageLengthHistogram(provider);
				histogram.addData(result);
				this.result.setCalculation(histogram);
				this.result.show();
			}
			if (result.isWordFrequencyCalculations()) {
				WordZipfFrequencyHistogram histogram = new WordZipfFrequencyHistogram(provider);
				histogram.addData(result);
				this.result.setCalculation(histogram);
				this.result.show();
			}
			if (result.isSentenceAverageCalculations()) {
				SentenceAverageLengthHistogram histogram = new SentenceAverageLengthHistogram(provider);
				histogram.addData(result);
				this.result.setCalculation(histogram);
				this.result.show();
			}
			quantitativeAnalysisTab.showLoading(false);
		});
	}

	public void showTimeSeriesResults(TimeSeriesResult data) {
		getUI().access(() -> {
			TimeSeriesChart time = new TimeSeriesChart(provider);
			time.addData(data);
			this.result.setCalculation(time);
			this.result.show();
			timeSeriesTab.showLoading(false);
		});
	}

	public void showSelectionDataResults(Optional<DataSelectionResult> result) {
		getUI().access(() -> {
			if (result.isPresent()) {
				dataSelectionPanel.showResults(result.get());
			}
		});
	}

	public void showDataExplorationWordFrequencyResults(List<WordFrequencyDTO> result) {
		getUI().access(() -> {
			FrequencyList freq = new FrequencyList(provider);
			freq.addData(result);
			this.result.setCalculation(freq);
			this.result.show();
			dataExplorationTab.showLoading(false);
		});
	}

	public void showDataExplorationConcordanceResults(List<ConcordanceDTO> result) {
		getUI().access(() -> {
			ConcordanceList concord = new ConcordanceList(provider);
			concord.addData(result);
			this.result.setCalculation(concord);
			this.result.show();
			dataExplorationTab.showLoading(false);
		});
	}

	public DataSelectionPanel getDataSelectionPanel() {
		return dataSelectionPanel;
	}

	public QuantitativeAnalysisTab getQuantitativeAnalysisTab() {
		return quantitativeAnalysisTab;
	}

	public TimeSeriesTab getTimeSeriesTab() {
		return timeSeriesTab;
	}

	public DataExplorationTab getDataExplorationTab() {
		return dataExplorationTab;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	public void showLocationMap() {
		NamesOnGoogleMap map = new NamesOnGoogleMap(provider);
		map.loadData(Arrays.asList("Wrocław", "Kraków", "Warszawa", "Szczecin"));
		this.result.setCalculation(map);
		this.result.show();
	}
}