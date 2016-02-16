package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.fields.MTable;

import java.util.Locale;

public class FilterPanel extends VerticalLayout {

    private final MTable table;
    private TextField filter;

    public FilterPanel() {
        setSpacing(true);
        filter = new TextField();
        initializeFilter();

        table = new MTable();
        initializeTable();
        addComponent(filter);
        addComponent(table);
    }

    private void initializeFilter() {
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setColumns(16);
        filter.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        filter.setLocale(new Locale("pl"));
        filter.setImmediate(true);
    }

    private void initializeTable() {
        table.addStyleName(ValoTheme.TABLE_NO_HEADER);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_NO_STRIPES);
        table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
    }
}
