package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.about.AboutView;
import pl.clarin.chronopress.presentation.page.admin.AdminView;
import pl.clarin.chronopress.presentation.page.admin.users.ChangePasswordEvent;
import pl.clarin.chronopress.presentation.page.education.EducationView;
import pl.clarin.chronopress.presentation.page.login.UserLoggedInEvent;
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
    javax.enterprise.event.Event<NavigationEvent> navigation;

    private final MenuBar menuBar = new MenuBar();

    private final MenuBar.MenuItem start = menuBar.addItem("START", null, (MenuBar.Command) i -> navigation.fire(new NavigationEvent((StartView.ID))));

    private final MenuBar.MenuItem samples = menuBar.addItem("PRZEGLĄD PRÓBEK", null,
            (MenuBar.Command) i -> navigation.fire(new NavigationEvent((SampleBrowserView.ID))));

    private final MenuBar.MenuItem about = menuBar.addItem("O KORPUSIE", null,
            (MenuBar.Command) i -> navigation.fire(new NavigationEvent(AboutView.ID)));

    private final MenuBar.MenuItem education = menuBar.addItem("EDUKACJA", null,
            (MenuBar.Command) i -> navigation.fire(new NavigationEvent(EducationView.ID)));

    private final MenuBar.MenuItem user = menuBar.addItem("LOGIN", null, null);

    @PostConstruct
    public void init() {
        setWidth(100, Unit.PERCENTAGE);

        menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.addStyleName(ChronoTheme.MENU_BAR);

        addSignInItem();

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

    public void addSignInItem() {
        user.addItem(provider.getProperty("menu.sign.in"), FontAwesome.SIGN_IN,
                (MenuBar.Command) i -> signInEvent.fire(new UserSignInEvent()));
    }

    public void onLoggendInEvent(@Observes(notifyObserver = Reception.IF_EXISTS) UserLoggedInEvent event) {
        user.removeChildren();
        user.setText(SecurityUtils.getSubject().getPrincipal().toString());
        user.addItem(provider.getProperty("menu.administration"), FontAwesome.COG, (MenuBar.Command) selected
                -> navigation.fire(new NavigationEvent(AdminView.ID)));

        user.addItem(provider.getProperty("menu.change.password"), FontAwesome.LOCK, (MenuBar.Command) selected -> {

            changePasswordEvent.fire(new ChangePasswordEvent(SecurityUtils.getSubject().getPrincipal().toString()));

        });

        user.addItem(provider.getProperty("menu.sign.out"), FontAwesome.SIGN_OUT,
                (MenuBar.Command) i -> {
                    user.removeChildren();
                    user.setText("");
                    addSignInItem();
                    loggedOutEvent.fire(new UserLoggedOutEvent());
                });
    }

}
