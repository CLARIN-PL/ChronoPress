package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class Branding extends HorizontalLayout{

    public Branding(){
        setSpacing(true);
        addStyleName(ChronoTheme.BRANDING);

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(new MarginInfo(false,true,false,false));

        layout.addComponents(addImage("img/logo_uw.png", 45), addImage("img/logo-clarin.png", 45));        
        addComponents(layout, addImage("img/logo_ktp.png", 90));
     
    }
    
    private Image addImage(String path, int height){
        Image img = new Image(null, new ThemeResource(path));
        img.setHeight(height, Unit.PIXELS);
        return img;
    }
}
