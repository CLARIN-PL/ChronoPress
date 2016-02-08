package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 04.02.16.
 */

@SpringComponent
@UIScope
public class Footer extends HorizontalLayout{

    @PostConstruct
    public void init(){
        setWidth(100, Sizeable.Unit.PERCENTAGE);
        addStyleName(ChronoTheme.FOOTER);

        HorizontalLayout content = initializeFooter();
        addComponent(content);
        setComponentAlignment(content , Alignment.BOTTOM_CENTER);
    }

    private HorizontalLayout initializeFooter(){
        HorizontalLayout layout = new HorizontalLayout();

        Label label = new Label("Copyright 2014 Clarin-PL. All Rights Reserved.");
        label.addStyleName(ValoTheme.LABEL_COLORED);
        layout.addComponent(label);
        return layout;
    }

}
