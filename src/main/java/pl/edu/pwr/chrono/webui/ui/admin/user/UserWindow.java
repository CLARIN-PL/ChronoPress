package pl.edu.pwr.chrono.webui.ui.admin.user;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.User;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
@Slf4j
public class UserWindow extends Window {

    @PropertyId("userName")
    private final TextField username = new TextField();

    @PropertyId("email")
    private final TextField email = new TextField();

    @PropertyId("password")
    private final PasswordField password = new PasswordField();

    private final PasswordField confirmPassword = new PasswordField();

    @PropertyId("active")
    private final CheckBox active = new CheckBox();

    private final BeanFieldGroup<User> binder = new BeanFieldGroup<>(User.class);

    private final Button save = new Button();
    private final Button cancel = new Button();

    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.user"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(55, Unit.PERCENTAGE);

        save.setCaption(provider.getProperty("button.save"));
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.setIcon(FontAwesome.SAVE);

        cancel.setCaption(provider.getProperty("button.cancel"));
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.setIcon(FontAwesome.TIMES);

        binder.bindMemberFields(this);
        setModal(true);
        center();
        setContent(buildForm());
    }

    public User getItem() {
        try {
            confirmPassword.validate();
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    public void setItem(User user) {
        binder.setItemDataSource(user);
    }

    private Component buildForm() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        FormLayout form = new FormLayout();
        form.addStyleName(ChronoTheme.COMPACT_FORM);
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(form);
        form.setWidth(100, Unit.PERCENTAGE);

        username.setCaption(provider.getProperty("label.username"));
        username.setNullRepresentation("");
        form.addComponent(username);

        email.setCaption(provider.getProperty("label.email"));
        email.setNullRepresentation("");
        form.addComponent(email);


        password.setCaption(provider.getProperty("label.password"));
        password.setNullRepresentation("");
        form.addComponent(password);

        confirmPassword.setCaption(provider.getProperty("label.confirm.password"));
        confirmPassword.setNullRepresentation("");
        confirmPassword.addValidator(value -> {
            if (!value.toString().equals(password.getValue())) {
                throw new Validator.InvalidValueException(provider.getProperty("validation.passwords.must.be.same"));
            }
        });
        form.addComponent(confirmPassword);

        active.setCaption(provider.getProperty("label.active"));
        form.addComponent(active);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth(100, Unit.PERCENTAGE);
        buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setSpacing(true);
        wrapper.addComponent(save);
        wrapper.addComponent(cancel);
        buttons.addComponent(wrapper);
        buttons.setComponentAlignment(wrapper, Alignment.MIDDLE_RIGHT);

        layout.addComponent(buttons);

        return layout;
    }

    public void reset() {
        confirmPassword.setValue("");
    }

    public Button getSave() {
        return save;
    }

    public Button getCancel() {
        return cancel;
    }
}
