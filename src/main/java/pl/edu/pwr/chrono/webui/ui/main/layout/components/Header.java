package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 04.02.16.
 */

@SpringComponent
@UIScope
public class Header  extends VerticalLayout{

    @Autowired  private MainMenu menu;
    @Autowired  private Branding branding;

    @PostConstruct
    public void init(){
        setWidth(100, Unit.PERCENTAGE);
        addStyleName(ChronoTheme.HEADER);

        addComponent(menu);
        addComponent(branding);
    }
}
