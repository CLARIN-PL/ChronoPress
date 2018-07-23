package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MMarginInfo;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class FilterBar extends HorizontalLayout{

    private MTextField filter;

    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(new MMarginInfo(false, true));
        filter = new MTextField()
                .withStyleName("inline-icon", ValoTheme.TEXTFIELD_TINY)
                .withIcon(FontAwesome.SEARCH);
        filter.setColumns(20);

        addComponent(filter);
    }

    public void addButton(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ChronoTheme.BUTTON);
        addComponent(btn);
        setComponentAlignment(btn, Alignment.TOP_RIGHT);
    }

    public void addValueChangeListener(Property.ValueChangeListener listener){
        filter.addValueChangeListener(listener);
    }
    
    public void addTextChangeListener(FieldEvents.TextChangeListener listener){
        filter.addTextChangeListener(listener);
    }
}
