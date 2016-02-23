package pl.edu.pwr.chrono.webui.ui.admin.caption;

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

    @Autowired
    private CaptionEditSubSection editSection;
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

        initializeContainer();
        filterBar = initializeFilterBar();
        initializeListeners();
        initializeView();

    }

    public void initializeView() {
        addComponent(filterBar);
        addComponent(editSection);
        addComponent(gridSection);
        editSection.hide();
    }

    private FilterBar initializeFilterBar() {
        FilterBar filterBar = new FilterBar();
        filterBar.addButton(refresh);
        refresh.setIcon(FontAwesome.REFRESH);
        refresh.setCaption(provider.getProperty("view.captions.list.refresh.button"));
        refresh.setVisible(false);
        return filterBar;
    }

    private void initializeContainer() {
        container = new BeanContainer<>(Property.class);
        container.setBeanIdProperty("id");
    }

    public Property getItem(Long id) {
        return container.getItem(id).getBean();
    }

    public CaptionEditSubSection getEditSection() {
        return editSection;
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
        gridSection.getGrid().setColumnOrder("key", "value");
        gridSection.getGrid().getColumn("key").setHeaderCaption(provider.getProperty("table.headers.key"));
        gridSection.getGrid().getColumn("value").setHeaderCaption(provider.getProperty("table.headers.value"));
        gridSection.getGrid().getColumn("lang").setHeaderCaption(provider.getProperty("table.headers.language"));
        gridSection.getGrid().getColumn("id").setHidden(true);
    }

    private void initializeListeners() {

        editSection.setSaveButtonClickListener(event -> {
            presenter.saveProperty();
        });

        editSection.setCancelButtonClickListener(event -> {
            presenter.cancel();
        });

        gridSection.getGrid().addItemClickListener(event -> {
            presenter.editItem((Long) event.getItemId());
        });

        refresh.addClickListener(event -> {
            presenter.refreshAction();
        });

    }

    public void showRefreshButton() {
        refresh.setVisible(true);
    }

    public void hideRefreshButton() {
        refresh.setVisible(false);
    }

    public GridSection getGridSection() {
        return gridSection;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        presenter.loadPropertyList();
    }
}
