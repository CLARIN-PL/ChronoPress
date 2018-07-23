package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class ResultSlot extends VerticalLayout {

    public ResultSlot(final String name, final Label value) {

        setSizeUndefined();
        addStyleName(ChronoTheme.PANEL_PLAIN);
        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        value.setSizeUndefined();
        value.addStyleName(ValoTheme.LABEL_HUGE);
        addComponent(value);

        Label title = new Label(name);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        addComponent(title);

    }
}