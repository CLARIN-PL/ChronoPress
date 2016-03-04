package pl.edu.pwr.chrono.webui.ui.admin.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import pl.edu.pwr.chrono.domain.User;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = UserView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class UserView extends DefaultView<UserPresenter> implements View {

    public static final String VIEW_NAME = "users";

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    private UserWindow userWindow;

    private MTable users;

    @Autowired
    public UserView(UserPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.users.title")));
        setSpacing(true);

        initUsersTable();
        initButtons();

        HorizontalLayout buttons = buildButtons();
        addComponent(buttons);
        addComponent(users);
    }

    private void initButtons() {
        userWindow.getCancel().addClickListener(event -> {
            userWindow.reset();
            UI.getCurrent().removeWindow(userWindow);
        });
        userWindow.getSave().addClickListener(event -> {
            presenter.saveUser(userWindow.getItem());
            loadUsers();
            userWindow.reset();
            UI.getCurrent().removeWindow(userWindow);
        });
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button createUser = new Button(provider.getProperty("button.create.user"));
        createUser.addStyleName(ValoTheme.BUTTON_TINY);
        createUser.addStyleName(ChronoTheme.BUTTON);
        createUser.setIcon(FontAwesome.PLUS_CIRCLE);
        createUser.addClickListener(event -> {
            userWindow.setItem(new User());
            UI.getCurrent().addWindow(userWindow);
        });

        buttons.addComponent(createUser);
        return buttons;
    }

    private void initUsersTable() {
        users = new MTable<>(User.class)
                .withProperties("userName", "email", "active")
                .withColumnHeaders(
                        provider.getProperty("label.username"),
                        provider.getProperty("label.email"),
                        provider.getProperty("label.active"))
                .withFullHeight().withFullWidth();
        users.addStyleName(ValoTheme.TABLE_COMPACT);
        users.addStyleName(ValoTheme.TABLE_SMALL);
        users.addStyleName(ValoTheme.TABLE_BORDERLESS);
        users.addStyleName(ChronoTheme.TABLE);
        users.setSelectable(true);
        users.addRowClickListener(event -> {
            userWindow.setItem((User) event.getEntity());
            UI.getCurrent().addWindow(userWindow);
        });
    }

    private void loadUsers() {
        users.removeAllItems();
        users.addBeans(presenter.getAllUsers());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        loadUsers();
    }
}
