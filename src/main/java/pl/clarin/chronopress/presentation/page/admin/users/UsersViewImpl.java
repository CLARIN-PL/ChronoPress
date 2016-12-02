package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.user.entity.User;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(UsersView.ID)
public class UsersViewImpl extends AbstractView<UsersViewPresenter> implements UsersView {

    @Inject
    Instance<UsersViewPresenter> presenterInstance;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    UserWindow userWindow;

    @Inject
    UserTable userTable;

    @PostConstruct
    public void init() {

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.users.title")),
                       buildButtons(), userTable);
        setCompositionRoot(layout);
    }

    private MHorizontalLayout buildButtons() {

        MButton createUser = new MButton(provider.getProperty("button.create.user"))
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    showUser(new User());
                });
        
         MButton removeUser = new MButton(provider.getProperty("button.remove.user"))
                .withIcon(FontAwesome.TRASH)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    User u = userTable.getSelectedUser();
                    if(u != null){
                       getPresenter().removeUser(u);
                       userTable.removeUser(u);
                    }
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(createUser, removeUser);

    }

    @Override
    protected UsersViewPresenter generatePresenter() {
        return presenterInstance.get();
    }

    @Override
    public void showUser(User user) {
         userWindow.setUser(user);
         UI.getCurrent().addWindow(userWindow);
    }
    
    @Override
    public void addUser(User user) {
        userTable.addUser(user);
    }

    @Override
    public void removeUser(User user) {
        userTable.removeUser(user);
    }

    @Override
    public void setUsers(List<User> allUsers) {
        userTable.setUsers(allUsers);
    }
}
