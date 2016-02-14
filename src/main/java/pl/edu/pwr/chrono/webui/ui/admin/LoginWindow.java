package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;


@SpringComponent
@UIScope
public class LoginWindow extends Window {

    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button signIn = new Button();
    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.login.title"));
        setModal(true);
        setResizable(false);
        setWidth(430, Unit.PIXELS);
        center();

        setContent(buildContent());
    }

    private VerticalLayout buildContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        HorizontalLayout fieldsWraper = new HorizontalLayout();
        fieldsWraper.setWidth(100, Unit.PERCENTAGE);
        fieldsWraper.setSpacing(true);
        fieldsWraper.setMargin(new MarginInfo(false, true, false, true));

        username.setCaption(provider.getProperty("label.username"));
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.addStyleName(ValoTheme.TEXTFIELD_TINY);
        username.focus();
        fieldsWraper.addComponent(username);

        password.setCaption(provider.getProperty("label.password"));
        password.setIcon(FontAwesome.KEY);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.addStyleName(ValoTheme.TEXTFIELD_TINY);
        fieldsWraper.addComponent(password);

        signIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signIn.addStyleName(ValoTheme.BUTTON_SMALL);
        signIn.setIcon(FontAwesome.SIGN_IN);
        signIn.setCaption(provider.getProperty("button.sign.in"));
        signIn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setMargin(true);
        wrapper.setWidth(100, Unit.PERCENTAGE);
        wrapper.addComponent(signIn);
        wrapper.setComponentAlignment(signIn, Alignment.MIDDLE_RIGHT);

        layout.addComponent(fieldsWraper);
        layout.addComponent(wrapper);
        return layout;
    }

    public String getPassword() {
        return password.getValue();
    }

    public String getUsername() {
        return username.getValue();
    }

    public Button getSignIn() {
        return signIn;
    }
}
