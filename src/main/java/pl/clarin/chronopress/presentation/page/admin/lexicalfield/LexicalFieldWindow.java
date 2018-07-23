package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class LexicalFieldWindow extends Window {

    @Inject
    LexicalFieldForm form;
    
    private Button save;
    private Button cancel;

    @Inject
    private DbPropertiesProvider provider;
    
    @Inject
    javax.enterprise.event.Event<SaveLexicalFieldEvent> saveEvent;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.lexical.field"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(55, Unit.PERCENTAGE);

        setModal(true);
        center();
        
        VerticalLayout layout = new MVerticalLayout()
                .withFullWidth()
                .with(form, buildButtons());
        
        setContent(layout);
    }

    public void setLexicalField(LexicalField lf){
        form.setItem(lf);
    }
    
   private HorizontalLayout buildButtons(){
       
        save = new MButton()
                .withCaption(provider.getProperty("button.save"))
                .withStyleName(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.SAVE)
                .withListener(l ->{
                    saveEvent.fire(new SaveLexicalFieldEvent(form.getItem()));
                    close();
                });
        

        cancel = new MButton()
                .withCaption(provider.getProperty("button.cancel"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.TIMES)
                .withListener(l -> {
                    form.cancel();
                    close();
                });
        

        HorizontalLayout wrapper = new MHorizontalLayout()
                    .withSpacing(true)
                    .with(save, cancel);

        return new MHorizontalLayout()
                .withFullWidth()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(wrapper)
                .withAlign(wrapper, Alignment.MIDDLE_RIGHT);
    }

}
