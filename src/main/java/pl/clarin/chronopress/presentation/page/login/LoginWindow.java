package pl.clarin.chronopress.presentation.page.login;

import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@UIScoped
public class LoginWindow extends Window {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<UserAuthenticationEvent> auth;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.login.title"));
        setModal(true);
        setResizable(false);
        setWidth(400, Unit.PIXELS);
        addStyleName(ChronoTheme.WINDOW);
        center();
        setContent(buildContent());
    }

    private VerticalLayout buildContent() {

        final MTextField username = new MTextField(provider.getProperty("label.username"))
                .withIcon(FontAwesome.USER)
                .withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                .withFullWidth();
        username.focus();

        final MPasswordField password = new MPasswordField(provider.getProperty("label.password"))
                .withIcon(FontAwesome.KEY)
                .withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                .withFullWidth();

        MVerticalLayout fieldsWraper = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(true)
                .withWidth("90%")
                .with(username, password);

        final MButton signIn = new MButton(provider.getProperty("button.sign.in"))
                .withIcon(FontAwesome.SIGN_IN)
                .withStyleName(ValoTheme.BUTTON_PRIMARY);
        signIn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signIn.addClickListener(e -> {
            auth.fire(new UserAuthenticationEvent(username.getValue(), password.getValue()));
        });

        MHorizontalLayout buttonwrapper = new MHorizontalLayout()
                .withFullWidth()
                .withMargin(true)
                .withMargin(true)
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(signIn)
                .withAlign(signIn, Alignment.MIDDLE_RIGHT);

        return new MVerticalLayout()
                .withSpacing(true)
                .withMargin(false)
                .with(fieldsWraper, buttonwrapper)
                .withAlign(fieldsWraper, Alignment.MIDDLE_CENTER);
    }

}
