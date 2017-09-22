package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.cdi.UIScoped;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.page.about.AboutView;
import pl.clarin.chronopress.presentation.page.admin.users.ChangePasswordEvent;
import pl.clarin.chronopress.presentation.page.login.UserLoggedOutEvent;
import pl.clarin.chronopress.presentation.page.login.UserSignInEvent;
import pl.clarin.chronopress.presentation.page.samplebrowser.SampleBrowserView;
import pl.clarin.chronopress.presentation.page.start.StartView;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@UIScoped
public class MainMenu extends HorizontalLayout {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<UserSignInEvent> signInEvent;

    @Inject
    javax.enterprise.event.Event<UserLoggedOutEvent> loggedOutEvent;

    @Inject
    javax.enterprise.event.Event<ChangePasswordEvent> changePasswordEvent;

    @Inject
    javax.enterprise.event.Event<ChangeLanguageEvent> changeLanguageEvent;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    private final MenuBar menuBar = new MenuBar();

    private final MenuBar.MenuItem start = menuBar.addItem("STRONA GŁÓWNA", null, (MenuBar.Command) i -> navigation.fire(new NavigationEvent((StartView.ID))));

    private final MenuBar.MenuItem samples = menuBar.addItem("PRZEGLĄD PRÓBEK", null,
            (MenuBar.Command) i -> navigation.fire(new NavigationEvent((SampleBrowserView.ID))));

    private final MenuBar.MenuItem about = menuBar.addItem("O KORPUSIE", null,
            (MenuBar.Command) i -> navigation.fire(new NavigationEvent(AboutView.ID)));

//    private final MenuBar.MenuItem lang = menuBar.addItem("Polski",
//            FontAwesome.FLAG, (MenuBar.MenuItem selectedItem) -> {
//
//                if (selectedItem.getText().equals("Polski")) {
//                    selectedItem.setText("English");
//                    changeLanguageEvent.fire(new ChangeLanguageEvent("EN"));
//                } else {
//                    selectedItem.setText("Polski");
//                    changeLanguageEvent.fire(new ChangeLanguageEvent("PL"));
//                }
//                UI.getCurrent().getNavigator().navigateTo("");
//            });
    @PostConstruct
    public void init() {
        Cookie lang = VaadinUI.getCookieByName("chronopress");
        if (lang != null) {

            if (lang.getValue().equals("PL")) {
                lang.setValue("Polski");
            } else {
                lang.setValue("English");
            }
        }

        setWidth(100, Unit.PERCENTAGE);

        menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.addStyleName(ChronoTheme.MENU_BAR);

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withStyleName(ChronoTheme.MENU_BAR_WRAPPER)
                .withMargin(new MarginInfo(false, false, false, true))
                .withSpacing(false)
                .with(menuBar);

        MHorizontalLayout layout = new MHorizontalLayout()
                .withSpacing(false)
                .with(wrapper);

        addComponent(layout);
        setComponentAlignment(layout, Alignment.BOTTOM_RIGHT);
    }

}
