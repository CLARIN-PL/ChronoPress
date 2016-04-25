package pl.edu.pwr.chrono.webui.ui.admin.stoplist;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.StopList;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = StopListView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class StopListView extends DefaultView<StopListPresenter> implements View {

    public static final String VIEW_NAME = "stop-list";
    private final Grid grid = new Grid();

    @Autowired
    private DbPropertiesProvider provider;
    private BeanItemContainer<StopList> container = new BeanItemContainer<>(StopList.class);

    @Autowired
    public StopListView(StopListPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.stop.list.title")));
        setSpacing(true);

        iniGridColumns();
        addComponent(buildAddNameSection());

        addComponent(grid);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
    }

    public HorizontalLayout buildAddNameSection() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        TextField name = new TextField();
        name.addStyleName(ValoTheme.TEXTAREA_TINY);

        Label label = new Label(provider.getProperty("label.name"));

        Button addName = new Button(provider.getProperty("label.add.stop.list.item.name"));
        addName.setIcon(FontAwesome.PLUS_CIRCLE);
        addName.addStyleName(ValoTheme.BUTTON_TINY);
        addName.addStyleName(ChronoTheme.BUTTON);

        addName.addClickListener(event -> {
                StopList item = new StopList();
                item.setName(name.getValue());
                presenter.save(item);
                container.addBean(item);
        });

        Button removeSelected = new Button(provider.getProperty("label.remove.stop.list.item.name"));
        removeSelected.setIcon(FontAwesome.TRASH_O);
        removeSelected.addStyleName(ValoTheme.BUTTON_TINY);
        removeSelected.addStyleName(ChronoTheme.BUTTON);
        removeSelected.addClickListener(event -> {
            if (grid.getSelectedRows().size() > 0) {
                grid.getSelectedRows().forEach(o -> {
                    presenter.delete((StopList) o);
                    container.removeItem(o);
                });
            }
        });

        layout.addComponent(label);
        layout.addComponent(name);
        layout.addComponent(addName);
        layout.addComponent(removeSelected);

        return layout;
    }

    private void iniGridColumns() {
        grid.setContainerDataSource(container);
        grid.getColumn("id").setHidden(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.removeAllItems();
        container.addAll(presenter.getStopList());
    }

}
