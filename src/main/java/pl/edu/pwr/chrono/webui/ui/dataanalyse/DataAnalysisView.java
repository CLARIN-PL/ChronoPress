package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SpringView(name = DataAnalysisView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class DataAnalysisView extends DefaultView<DataAnalysisPresenter> implements View {

	public static final String VIEW_NAME = "analyse";

	@Autowired
	private DbPropertiesProvider provider;

	@Autowired
	private DataSelectionTab dataSelectionTab;

	@Autowired
	private QuantitativeAnalysisTab quantitativeAnalysisTab;

	@Autowired
	public DataAnalysisView(DataAnalysisPresenter presenter) {
		super(presenter);
	}

	private final TabSheet sheet = new TabSheet();

	@PostConstruct
	public void init() {
		addComponent(new Title(FontAwesome.COGS, provider.getProperty("view.data.analyse.title")));
		sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		addComponent(sheet);
		initializeListeners();
		initDataSelectionTab();
		initQuantitativeAnalysisTab();
	}


	private void initDataSelectionTab(){
		dataSelectionTab.getYears().addItems(presenter.loadYears());
		dataSelectionTab.getTitles().addItems(presenter.loadTitles());
		dataSelectionTab.getExpositions().addItems(presenter.loadExpositions());
		dataSelectionTab.getPeriods().addItems(presenter.loadPeriods());
		dataSelectionTab.getSearchAuthorsPanel().populateContainer(presenter.loadAuthors());
		sheet.addComponent(dataSelectionTab);
	}

	private void initQuantitativeAnalysisTab(){
		sheet.addComponent(quantitativeAnalysisTab);
	}

	public void initializeListeners(){
		dataSelectionTab.getAcceptButton().addClickListener(e -> {
			presenter.onAcceptDataSelection();
		});

		quantitativeAnalysisTab.getAcceptButton().addClickListener(event -> {
			presenter.onQuantitativeAnalysis();
		});

	}
	public void showSelectionDataResults(Optional<DataSelectionResult> result) {
		getUI().access(() -> {
			if (result.isPresent()) {
				dataSelectionTab.showResults(result.get().getSampleCount(), result.get().getWordCount());
			}
		});
	}

	public DataSelectionTab getDataSelectionTab() {
		return dataSelectionTab;
	}

	public QuantitativeAnalysisTab getQuantitativeAnalysisTab() {
		return quantitativeAnalysisTab;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}