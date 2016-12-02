package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import pl.clarin.chronopress.presentation.shered.dto.AuthorDTO;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class SearchableTablePanel extends VerticalLayout {

    private final Grid grid;
    private final IndexedContainer container;
    private TextField filter;

    public SearchableTablePanel() {
        setSpacing(true);
        filter = new TextField();
        initializeFilter();

        container = new IndexedContainer();
        container.addContainerProperty("name", String.class, null);

        grid = new Grid();
        initializeGrid();
        addComponent(filter);
        addComponent(grid);
    }

    private void initializeFilter() {
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setWidth("30%");
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

    private void initializeGrid() {
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("name");
        grid.getColumn("name").setHeaderCaption("Autor");
    }

    public void clearSelection() {
        grid.getSelectionModel().reset();
    }

    public List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        grid.getSelectedRows().forEach(o -> selected.add(((AuthorDTO) o).getName()));
        return selected;
    }

    public void populateContainer(List<String> items) {
        items.forEach(i -> {
            Item newItem = container.getItem(container.addItem());
            newItem.getItemProperty("name").setValue(i);
        });
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        return val.startsWith(text.toLowerCase().trim());
    }

    private class TextFilter implements Container.Filter {

        private String needle;

        public TextFilter(String needle) {
            this.needle = needle;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item)
                throws UnsupportedOperationException {
            return needle == null || "".equals(needle) || filterByProperty("name", item, needle);
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            return propertyId.equals("name");
        }
    }

}
