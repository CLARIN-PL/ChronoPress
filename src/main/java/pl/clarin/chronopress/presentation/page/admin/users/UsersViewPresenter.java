package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.user.boundary.UserFacade;
import pl.clarin.chronopress.business.user.entity.User;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class UsersViewPresenter extends AbstractPresenter<UsersView> {

    @Inject
    UserFacade userFacade;

    @Override
    protected void onViewEnter() {
        getView().setUsers(userFacade.findAll());
    }

    public void onShowUserDetails(@Observes(notifyObserver = Reception.IF_EXISTS) ShowUserDetailsEvent event) {
        getView().showUser(event.getUser());
    }

    public void onSaveUser(@Observes(notifyObserver = Reception.IF_EXISTS) SaveUserEvent event) {
        try {
            User user = event.getUser();
            if (event.getPassword() != null && !"".equals(event.getPassword())) {
                user.setPassword(event.getPassword());
            }
            user = userFacade.save(user);
            getView().removeUser(event.getUser());
            getView().addUser(user);
            Notification.show("Użytkownik " + event.getUser().getUsername() + " pomyślnie zapisany", Notification.Type.TRAY_NOTIFICATION);
        } catch (RuntimeException ex) {
            Notification.show("Zapisanie użytkownika " + event.getUser().getUsername() + " nie powiodło się", Notification.Type.TRAY_NOTIFICATION);
        }

    }

    public void removeUser(User u) {
        userFacade.remove(u);
    }
}
