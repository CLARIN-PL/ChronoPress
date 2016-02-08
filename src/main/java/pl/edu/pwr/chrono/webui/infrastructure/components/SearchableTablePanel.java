package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by tnaskret on 08.02.16.
 */

@SpringComponent
@ViewScope
public class SearchableTablePanel extends VerticalLayout{

    private TextField filter;

    private final Grid grid;
    private final BeanItemContainer<StringBeanItem> container;

    public SearchableTablePanel() {
        setSpacing(true);
        filter = new TextField();
        initializeFilter();

        container = new BeanItemContainer<>(StringBeanItem.class);

        grid = new Grid();
        initializeGrid();
        addComponent(filter);
        addComponent(grid);
    }

    private void initializeFilter() {
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setColumns(25);
        filter.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        filter.setLocale(new Locale("pl"));
        filter.setImmediate(true);
        filter.addValueChangeListener(event -> {
            container.removeAllContainerFilters();
            container.addContainerFilter(
                    new TextFilter(event.getProperty().getValue().toString()));
        });
        filter.addTextChangeListener(
                event -> {
                    container.removeAllContainerFilters();
                    container.addContainerFilter(
                            new TextFilter(event.getText()));
                });
    }

    private void initializeGrid(){
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("value");
        grid.getColumn("value").setHeaderCaption("Autor");
    }

    public List<String> getSelectedItems(){
       List<String> selected = Lists.newArrayList();
       Collection<Object> list = grid.getSelectedRows();
       list.forEach(o -> {
           selected.add( ((StringBeanItem) o).getValue());
       });
        return selected;
    }

    public void populateContainer(List<String> items){
        List<StringBeanItem> list = Lists.newArrayList();
        items.forEach(s -> {
            list.add(new StringBeanItem(s));
        });
        container.addAll(list);
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null)
            return false;
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.startsWith(text.toLowerCase().trim())) return true;
        return false;
    }

    private class TextFilter implements Container.Filter {
        private String needle;

        public TextFilter(String needle) {
            this.needle = needle;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item)
                throws UnsupportedOperationException {
            if (needle == null || "".equals(needle)) {
                return true;
            }
            return filterByProperty("value", item, needle);

        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            if (propertyId.equals("value"))
                return true;
            return false;
        }
    }


}
