package pl.edu.pwr.chrono.webui.ui.admin.caption;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Property;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.FilterBar;
import pl.edu.pwr.chrono.webui.infrastructure.components.GridSection;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by tnaskret on 30.12.15.
 */

@SpringView(name = CaptionsView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class CaptionsView extends DefaultView<CaptionPresenter> implements View {

    public static final String VIEW_NAME = "labels";
    private final Button refresh = new Button();

    private BeanContainer<Long, Property> container;

    @Autowired
    private GridSection gridSection;

    @Autowired
    private DbPropertiesProvider provider;
    private FilterBar filterBar;

    @Autowired
    public CaptionsView(CaptionPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.caption.editor.title")));
        setSpacing(true);
        initializeContainer();
        filterBar = initializeFilterBar();
        filterBar.getFilter().addValueChangeListener(event -> {
            container.removeAllContainerFilters();
            container.addContainerFilter(
                    new TextFilter(event.getProperty().getValue().toString()));
        });
        filterBar.getFilter().addTextChangeListener(
                event -> {
                    container.removeAllContainerFilters();
                    container.addContainerFilter(
                            new TextFilter(event.getText()));
                });

        initializeListeners();
        initializeView();
    }

    public void initializeView() {
        addComponent(filterBar);
        addComponent(gridSection);
    }

    private FilterBar initializeFilterBar() {
        FilterBar filterBar = new FilterBar();
        filterBar.addButton(refresh);
        refresh.setIcon(FontAwesome.REFRESH);
        refresh.setCaption(provider.getProperty("view.captions.list.refresh.button"));
        return filterBar;
    }

    private void initializeContainer() {
        container = new BeanContainer<>(Property.class);
        container.setBeanIdProperty("id");
    }

    public void loadPropertyList(List<Property> list) {
        populateContainer(list);
        refreshGrid();
        getGridSection().showGrid();
    }

    private void populateContainer(List<Property> list) {
        container.removeAllItems();
        container.addAll(list);
    }

    private void refreshGrid() {
        gridSection.getGrid().setContainerDataSource(container);
        iniGridColumns();
    }

    private void iniGridColumns() {
        gridSection.getGrid().setEditorEnabled(true);
        gridSection.getGrid().setColumnOrder("key", "value");
        gridSection.getGrid().getColumn("key").setHeaderCaption(provider.getProperty("table.headers.key"));
        gridSection.getGrid().getColumn("value").setHeaderCaption(provider.getProperty("table.headers.value"));
        gridSection.getGrid().getColumn("lang").setHeaderCaption(provider.getProperty("table.headers.language"));
        gridSection.getGrid().getColumn("id").setHidden(true);
        gridSection.getGrid().setEditorFieldGroup(new BeanFieldGroup<>(Property.class));
        gridSection.getGrid().getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {

            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                presenter.saveProperty(container.getItem(getGridSection().getGrid().getEditedItemId()).getBean());
            }
        });
    }

    private void initializeListeners() {

        refresh.addClickListener(event -> {
            presenter.refreshAction();
        });

    }

    public GridSection getGridSection() {
        return gridSection;
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null)
            return false;
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        return val.startsWith(text.toLowerCase().trim());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        presenter.loadPropertyList();
    }

    private class TextFilter implements Container.Filter {
        private String needle;

        public TextFilter(String needle) {
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
    }
}
