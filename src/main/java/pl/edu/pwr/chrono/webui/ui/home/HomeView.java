package pl.edu.pwr.chrono.webui.ui.home;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = HomeView.VIEW_NAME, ui = MainUI.class)
public class HomeView extends DefaultView<HomePresenter> implements View {

	public static final String VIEW_NAME = "";

	@Autowired
	public HomeView(HomePresenter presenter) {
		super(presenter);
	}

	private VerticalLayout mainSection = new VerticalLayout();
	private HorizontalLayout subSection = new HorizontalLayout();

	@PostConstruct
	public void init() {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(true);
		setSpacing(true);

		initializeMainSection();
		initializeSubSection();

		mainSection.setWidth(100, Unit.PERCENTAGE);
		mainSection.setMargin(new MarginInfo(true));
		subSection.setWidth(100, Unit.PERCENTAGE);
		subSection.setMargin(true);

		addComponent(mainSection);
		addComponent(subSection);

		setComponentAlignment(mainSection, Alignment.MIDDLE_CENTER);
		setComponentAlignment(subSection, Alignment.MIDDLE_CENTER);

	}

	private void initializeMainSection(){

		Panel panel = new Panel("O projekcje");
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.addStyleName(ChronoTheme.LEAD_PANEL);
		panel.setContent(panelContent(
				"Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. " +
				"Praesent id metus massa, ut blandit odio."+
				"Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. " +
						"Praesent id metus massa, ut blandit odio."+
				"Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. " +
						"Praesent id metus massa, ut blandit odio."+
				"Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. " +
						"Praesent id metus massa, ut blandit odio."
		));

		mainSection.addComponent(panel);
	}

	private void initializeSubSection(){

		Panel tools = new Panel("Narzędzia");
		tools.setIcon(FontAwesome.WRENCH);
		tools.addStyleName(ChronoTheme.SUB_PANEL);
		tools.addStyleName("borderless");
		tools.setContent(panelContent("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio."));

		Panel corpus = new Panel("Korpus");
		corpus.setIcon(FontAwesome.BOOK);
		corpus.addStyleName(ChronoTheme.SUB_PANEL);
		corpus.addStyleName("borderless");
		corpus.setContent(panelContent("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio."));

		Panel team = new Panel("Zaspół");
		team.setIcon(FontAwesome.GROUP);
		team.addStyleName(ChronoTheme.SUB_PANEL);
		team.addStyleName("borderless");
		team.setContent(panelContent("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio."));

		subSection.addComponent(tools);
		subSection.addComponent(corpus);
		subSection.addComponent(team);
	}

	private Component panelContent(String text) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label content = new Label(text);
		layout.addComponent(content);
		return layout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}