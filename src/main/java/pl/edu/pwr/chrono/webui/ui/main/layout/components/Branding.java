package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
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

        VerticalLayout v = new VerticalLayout();
        v.setSpacing(true);
        v.setMargin(new MarginInfo(false,true,false,false));
        Image UWLogo = new Image(null, new ThemeResource("img/logo_uw.png"));
        UWLogo.setHeight(45, Unit.PIXELS);
        Image clarinLogo = new Image(null, new ThemeResource("img/logo-clarin.png"));
        clarinLogo.setHeight(45, Unit.PIXELS);

        v.addComponent(UWLogo);
        v.addComponent(clarinLogo);

        Image logo = new Image(null, new ThemeResource("img/logo_ktp.png"));
        logo.setHeight(90, Unit.PIXELS);
        addComponent(v);
        addComponent(logo);
    }
}
