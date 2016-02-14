package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Title extends HorizontalLayout{

    public Title(FontAwesome icon, String title) {
        initTitle(title, icon, ChronoTheme.TITLE);
    }

    public Title(String title) {
        initTitle(title, null, ChronoTheme.TITLE_SMALL);
    }

    public Title(String title, String style) {
        initTitle(title, null, style);
    }

    private void initTitle(String title, FontAwesome icon, String style) {
        setWidth(100, Unit.PERCENTAGE);
        addStyleName(style);

        Label desc = new Label();
        desc.setCaption(title);
        if (icon != null) desc.setIcon(icon);
        addComponent(desc);
    }

}
