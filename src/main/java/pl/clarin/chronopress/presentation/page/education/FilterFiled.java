/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.education;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.vaadin.viritin.fields.MTextField;

public class FilterFiled extends CustomComponent {

    private MTextField filter;

    @PostConstruct
    public void init() {

        filter = new MTextField()
                .withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON, ValoTheme.TEXTFIELD_TINY)
                .withIcon(FontAwesome.SEARCH)
                .withFullWidth();

        filter.setLocale(new Locale("pl"));
        filter.setImmediate(true);
        filter.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);

        setCompositionRoot(filter);
    }

    public void addValueChangListener(Field.ValueChangeListener listener) {
        filter.addValueChangeListener(listener);
    }

    public void addTextChangListener(FieldEvents.TextChangeListener listener) {
        filter.addTextChangeListener(listener);
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        return val.startsWith(text.toLowerCase().trim());
    }

    public class TextFilter implements Container.Filter {

        private String needle;
        private String property;

        public TextFilter(String needle, String property) {
            this.needle = needle;
            this.property = property;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item)
                throws UnsupportedOperationException {
            return needle == null || "".equals(needle) || filterByProperty(property, item, needle);
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            return propertyId.equals(property);
        }
    }

}
