package pl.clarin.chronopress.presentation.page.admin.audience;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class AudienceWindow extends Window {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    AudienceForm form;
    
    @Inject
    javax.enterprise.event.Event<AudienceSaveEvent> saveEvent;
    
    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.audience"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(55, Unit.PERCENTAGE);
        setModal(true);
        center();

        MVerticalLayout layout = new MVerticalLayout()
                .withFullWidth()
                .with(form, buildButtons());
        
        setContent(layout);
    }

    private HorizontalLayout buildButtons() {

        final Button save = new MButton()
                .withCaption(provider.getProperty("button.save"))
                .withStyleName(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.SAVE)
                .withListener(l -> {
                    saveEvent.fire(new AudienceSaveEvent(form.getItem()));
                    cleanUp();
                });

        final Button cancel = new MButton()
                .withCaption(provider.getProperty("button.cancel"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.TIMES)
                .withListener(l -> {
                    cleanUp();
                });

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withSpacing(true)
                .with(save, cancel);

        return new MHorizontalLayout()
                .withFullWidth()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(wrapper)
                .withAlign(wrapper, Alignment.MIDDLE_RIGHT);
    }
    
    public void cleanUp(){
        form.cleanUp();
        close();
    }
    
    public void setAudience(Audience audience){
        form.setItem(audience);
    }
    
     public void setReferenceNames(List<String> list){
        form.setReferenceNames(list);
    }
}
