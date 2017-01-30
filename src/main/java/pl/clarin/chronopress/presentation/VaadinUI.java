package pl.clarin.chronopress.presentation;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;
import pl.clarin.chronopress.presentation.page.error.ErrorView;
import pl.clarin.chronopress.presentation.page.login.AutenthicationPresenter;
import pl.clarin.chronopress.presentation.page.start.StartView;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.layout.MainLayout;

@CDIUI("")
@Title("ChronoPress")
@Theme("press")
@Widgetset("pl.clarin.chronopress.Widgetset")
@Push(transport = Transport.WEBSOCKET_XHR)
public class VaadinUI extends UI {

    @Inject
    CDIViewProvider viewProvider;

    @Inject
    MainLayout layout;

    @Inject
    ErrorView errorView;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    AutenthicationPresenter auth;

    @Override
    protected void init(VaadinRequest request) {
        setContent(layout);

        GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker("UA-61350840-4", "chronopress.clarin-pl.eu");
        tracker.extend(this);

        Navigator navigator = new Navigator(this, layout.getContent());
        navigator.addProvider(viewProvider);
        navigator.setErrorView(errorView);
        navigator.addViewChangeListener(tracker);
        navigator.navigateTo(StartView.ID);

    }

    public void onNavigationEvent(@Observes(notifyObserver = Reception.IF_EXISTS) NavigationEvent event) {
        try {
            getNavigator().navigateTo(event.getNavigateTo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
