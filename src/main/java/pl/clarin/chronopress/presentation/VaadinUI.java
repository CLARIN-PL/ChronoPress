package pl.clarin.chronopress.presentation;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.error.ErrorView;
import pl.clarin.chronopress.presentation.page.login.LoginViewPresenter;
import pl.clarin.chronopress.presentation.page.start.StartView;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.layout.ChangeLanguageEvent;
import pl.clarin.chronopress.presentation.shered.layout.MainLayout;

@CDIUI("")
@Title("ChronoPress")
@Theme("press")
@Widgetset("pl.clarin.chronopress.Widgetset")
@Push(transport = Transport.LONG_POLLING)
public class VaadinUI extends UI {

    @Inject
    CDIViewProvider viewProvider;

    @Inject
    MainLayout layout;

    @Inject
    DbPropertiesProvider propertiesProvider;

    @Inject
    ErrorView errorView;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    LoginViewPresenter auth;

    @Override
    protected void init(VaadinRequest request) {
        loadLanguage();
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

    public static String infoMessage(String content) {
        return "<div style=\"margin: 4px;font-size: 13px;\">" + content + "</div>";
    }

    public void onChangeLanguage(@Observes(notifyObserver = Reception.IF_EXISTS) ChangeLanguageEvent evn) {

        if ("PL".equals(evn.getLnag())) {
            updateCookieValue("chronopress", "PL");
        } else {
            updateCookieValue("chronopress", "EN");
        }
        getNavigator().navigateTo(StartView.ID);
    }

    private void loadLanguage() {

        Cookie lang = getCookieByName("chronopress");

        if (lang != null) {

            if (lang.getValue().equals("PL")) {
                propertiesProvider.loadProperties("PL");
            } else {
                propertiesProvider.loadProperties("EN");
            }

        } else {
            setLangCookie("EN");
            propertiesProvider.loadProperties("EN");
        }

    }

    public static void setLangCookie(String value) {
        // Create a new cookie
        Cookie myCookie = new Cookie("chronopress", value);

        // Make cookie expire in 2 minutes
        myCookie.setMaxAge(99999);

        // Set the cookie path.
        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

        // Save cookie
        VaadinService.getCurrentResponse().addCookie(myCookie);
    }

    public static Cookie updateCookieValue(final String name, final String value) {
        // Create a new cookie
        Cookie cookie = getCookieByName(name);

        cookie.setValue(value);

        // Save cookie
        VaadinService.getCurrentResponse().addCookie(cookie);

        return cookie;
    }

    public static Cookie getCookieByName(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        Cookie chronopressCookie = null;

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                chronopressCookie = cookie;
            }
        }

        return chronopressCookie;
    }

}
