package pl.edu.pwr.chrono.webui.ui.main;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.event.NavigationEvent;
import pl.edu.pwr.chrono.webui.infrastructure.event.UIEventBus;
import pl.edu.pwr.chrono.webui.ui.main.layout.MainLayout;

import java.util.Locale;

@Title("ChronoPress")
@Theme("chrono")
@SpringUI
@Push(transport = Transport.LONG_POLLING)
@Widgetset("pl.edu.pwr.config.Widgetset")
public final class MainUI extends UI {

	@Autowired
	private UIEventBus uiEventBus;

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private MainLayout layout;

	public static MainUI getCurrent() {
		return (MainUI) UI.getCurrent();
	}

	@Override
	protected void init(VaadinRequest request) {
		setLocale(new Locale("pl", "PL"));
		setContent(layout);
		Responsive.makeResponsive(this);

		Navigator navigator = new Navigator(this, layout.getContent());
		navigator.addProvider(viewProvider);
		navigator.setErrorView(ErrorView.class);
		setNavigator(navigator);

		uiEventBus.register(this);
	}

	@Subscribe
	public void navigateTo(NavigationEvent view) {
		getNavigator().navigateTo(view.getViewName());
	}
}
