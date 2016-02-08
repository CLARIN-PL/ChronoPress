package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Created by tnaskret on 05.02.16.
 */

public class Title extends HorizontalLayout{

    public Title(FontAwesome icon ,String title){

        setWidth(100, Unit.PERCENTAGE);
        addStyleName(ChronoTheme.TITLE);

        Label desc = new Label();
        desc.setCaption(title);
        desc.setIcon(icon);

        addComponent(desc);
    }
}
