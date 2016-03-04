package pl.edu.pwr.chrono.webui.ui.admin.user;

import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
@Slf4j
public class ChangePasswordWindow extends Window {

    private final PasswordField password = new PasswordField();

    private final PasswordField confirmPassword = new PasswordField();

    private final Button save = new Button();
    private final Button cancel = new Button();

    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.change.password"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(35, Unit.PERCENTAGE);

        save.setCaption(provider.getProperty("button.save"));
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.setIcon(FontAwesome.SAVE);

        cancel.setCaption(provider.getProperty("button.cancel"));
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.setIcon(FontAwesome.TIMES);

        setModal(true);
        center();
        setContent(buildForm());
    }

    public String getItem() {
        confirmPassword.validate();
        return confirmPassword.getValue();
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

        password.setCaption(provider.getProperty("label.password"));
        password.setNullRepresentation("");
        form.addComponent(password);

        confirmPassword.setCaption(provider.getProperty("label.confirm.password"));
        confirmPassword.setNullRepresentation("");
        confirmPassword.setImmediate(true);
        confirmPassword.addValidator(value -> {
            if (!value.toString().equals(password.getValue())) {
                throw new Validator.InvalidValueException(provider.getProperty("validation.passwords.must.be.same"));
            }
        });
        form.addComponent(confirmPassword);

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
        password.setValue("");
        confirmPassword.setValue("");
    }

    public Button getSave() {
        return save;
    }

    public Button getCancel() {
        return cancel;
    }
}
