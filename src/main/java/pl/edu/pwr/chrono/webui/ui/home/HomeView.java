package pl.edu.pwr.chrono.webui.ui.home;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
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
	private Label content = new Label();

	@PostConstruct
	public void init() {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(true);
		setSpacing(true);

		mainSection.setWidth(100, Unit.PERCENTAGE);
		mainSection.addComponent(content);
		content.setContentMode(ContentMode.HTML);
		content.setSizeFull();
		content.setValue(presenter.getHomePage().getContent());

		addComponent(mainSection);
		setComponentAlignment(mainSection, Alignment.MIDDLE_CENTER);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}