package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 04.02.16.
 */

@SpringComponent
@UIScope
public class Branding extends HorizontalLayout{

    @PostConstruct
    public void init(){
        setSpacing(true);
        addStyleName(ChronoTheme.BRANDING);

        Image logo = new Image(null, new ThemeResource("img/logo_ktp.png"));
        Image UWlogo = new Image(null, new ThemeResource("img/logo_uw.png"));

        addComponent(logo);
        addComponent(UWlogo);
    }
}
