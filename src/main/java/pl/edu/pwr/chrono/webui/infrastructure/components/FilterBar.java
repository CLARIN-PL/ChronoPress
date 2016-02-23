package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by tnaskret on 01.01.16.
 */

@SpringComponent
@ViewScope
public class FilterBar extends HorizontalLayout {

    private final TextField filter;

    public FilterBar() {
        setMargin(true);
        setSizeFull();

        filter = new TextField();
        filter.addStyleName(ChronoTheme.TEXTFIELD_ROUND);
        filter.addStyleName("inline-icon");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);

        addComponent(filter);
    }

    public void addButton(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ChronoTheme.BUTTON);
        addComponent(btn);
        setComponentAlignment(btn, Alignment.TOP_RIGHT);
    }

}
