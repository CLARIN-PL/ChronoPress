package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;

import javax.annotation.PostConstruct;

@SpringView(name = DataAnalyseView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class DataAnalyseView extends DefaultView<DataAnalysePresenter> implements View {

	public static final String VIEW_NAME = "analyse";

	@Autowired
	private DataSelectionTab dataSelectionTab;

	@Autowired
	public DataAnalyseView(DataAnalysePresenter presenter) {
		super(presenter);
	}

	@PostConstruct
	public void init() {
		setMargin(true);

		addComponent(new Title(FontAwesome.COGS, "Analiza danych"));

		TabSheet sheet = new TabSheet();
		sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);

		sheet.addComponent(dataSelectionTab);
		addComponent(sheet);

		initializeListeners();
	}

	public void initializeListeners(){
		dataSelectionTab.getAcceptButton().addClickListener(e -> {
			presenter.acceptDataSelection();
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}