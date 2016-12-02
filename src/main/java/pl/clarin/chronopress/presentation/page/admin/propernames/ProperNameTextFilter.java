/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.propernames;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class ProperNameTextFilter implements Container.Filter {

    private String needle;

    public ProperNameTextFilter(String needle) {
        this.needle = needle;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item)
            throws UnsupportedOperationException {
        return needle == null || "".equals(needle)
                || filterByProperty("base", item, needle)
                || filterByProperty("orth", item, needle);
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId.equals("base") || propertyId.equals("orth");
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        return val.startsWith(text.toLowerCase().trim());
    }
}
