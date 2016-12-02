package pl.clarin.chronopress.presentation.page.login;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import pl.clarin.chronopress.business.user.boundary.UserFacade;
import pl.clarin.chronopress.presentation.page.admin.AdminView;
import pl.clarin.chronopress.presentation.page.admin.users.ChangePasswordEvent;
import pl.clarin.chronopress.presentation.page.admin.users.ChangePasswordWindow;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;

@UIScoped
public class AutenthicationPresenter {

    @Inject
    javax.enterprise.event.Event<UserLoggedInEvent> logged;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    LoginWindow loginWindow;

    @Inject
    ChangePasswordWindow changePasswordWindow;

    @Inject
    UserFacade userFacade;

    @PostConstruct
    public void init() {
    }

    public void onAuthenticateEvent(@Observes(notifyObserver = Reception.IF_EXISTS) UserAuthenticationEvent event) {

        UsernamePasswordToken token = new UsernamePasswordToken(event.getUsername(), event.getPassword());

        try {

            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            if (subject.isAuthenticated()) {
                logged.fire(new UserLoggedInEvent());
                navigation.fire(new NavigationEvent(AdminView.ID));
                UI.getCurrent().removeWindow(loginWindow);
            }

        } catch (AuthenticationException e) {
            Notification.show("Błędne hasło lub login", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    public void onLoggOutEvent(@Observes(notifyObserver = Reception.IF_EXISTS) UserLoggedOutEvent event) {
        SecurityUtils.getSubject().logout();
        VaadinSession.getCurrent().close();
        Page.getCurrent().setLocation("");
    }

    public void onLoggedIn(@Observes(notifyObserver = Reception.IF_EXISTS) UserSignInEvent event) {
        UI.getCurrent().addWindow(loginWindow);
    }

    public void onUserChangePassword(@Observes(notifyObserver = Reception.IF_EXISTS) ChangePasswordEvent event) {
        changePasswordWindow.setUsername(event.getUsername());
        UI.getCurrent().addWindow(changePasswordWindow);
    }

    public void onSavePassword(@Observes(notifyObserver = Reception.IF_EXISTS) SavePasswordEvent event) {
        if (userFacade.changeUserPassword(event.getUsername(), event.getPassword())) {
            Notification.show("Hasło zostało zmienione", Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Zmiana hasła nie powiodła się", Notification.Type.TRAY_NOTIFICATION);
        }
    }
}
