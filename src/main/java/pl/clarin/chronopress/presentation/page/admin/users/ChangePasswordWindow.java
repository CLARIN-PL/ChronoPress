package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.login.SavePasswordEvent;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class ChangePasswordWindow extends Window {

    private String username;
    
    @Inject
    javax.enterprise.event.Event<SavePasswordEvent> savePasswordEvent;

    @Inject
    DbPropertiesProvider provider;
    
    @Inject
    PasswordForm passwordForm;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.change.password"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(35, Unit.PERCENTAGE);

        setModal(true);
        center();
        setContent(buildForm());
    }

    public void setUsername(String username){
        this.username =  username;
    }
    
    private HorizontalLayout buildButtons() {

        final Button save = new Button();
        final Button cancel = new Button();

        save.setCaption(provider.getProperty("button.save"));
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.setIcon(FontAwesome.SAVE);
        save.addClickListener(e -> {
            try {
               savePasswordEvent.fire(new SavePasswordEvent(username, passwordForm.getPssword()));
              cleanup();
            } catch(Exception ex){
               log.trace("Password validation error", ex);
            }                 
        });

        cancel.setCaption(provider.getProperty("button.cancel"));
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.setIcon(FontAwesome.TIMES);
        cancel.addClickListener(e -> {
            cleanup();
        });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(save, cancel);
    }

    private void cleanup(){
         passwordForm.reset();
         username = null;
         close();
    }

    private Component buildForm() {

        HorizontalLayout wrapper = buildButtons();

        MHorizontalLayout buttons = new MHorizontalLayout()
                .withFullWidth()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(wrapper)
                .withAlign(wrapper, Alignment.MIDDLE_RIGHT);

        return new MVerticalLayout()
                .withMargin(true)
                .withSpacing(true)
                .with(passwordForm, buttons);
    }


}
