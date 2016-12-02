/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.labels;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class LabelsTextFilter implements Container.Filter {

    private String needle;

    public LabelsTextFilter(String needle) {
        this.needle = needle;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item)
            throws UnsupportedOperationException {
        return needle == null || "".equals(needle)
                || filterByProperty("key", item, needle)
                || filterByProperty("value", item, needle);
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId.equals("key") || propertyId.equals("value");
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
