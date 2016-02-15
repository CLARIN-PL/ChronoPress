package pl.edu.pwr.chrono.webui.ui.samplebrowser;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.ListContainer;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = SampleBrowserView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class SampleBrowserView extends DefaultView<SampleBrowserPresenter> implements View {

    public static final String VIEW_NAME = "sample-browser";
    private final Grid grid = new Grid();
    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    public SampleBrowserView(SampleBrowserPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(FontAwesome.BOOK, provider.getProperty("view.sample.browser.title")));
        setSpacing(true);
        setWidth(100, Unit.PERCENTAGE);
        initializeGrid();

        grid.setSizeFull();
        addComponent(grid);

        grid.addSelectionListener(event -> {
            try {
                Notification.show(event.getSelected().toString());
            } catch (Exception e) {

            }
        });
    }

    private void initializeGrid() {
        grid.setContainerDataSource(
                new ListContainer<>(new LazyList<>(presenter, 50)));
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(20);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder("source", "title", "author", "publishDate", "style");
        grid.getColumn("source").setHeaderCaption(provider.getProperty("label.source"));
        grid.getColumn("title").setHeaderCaption(provider.getProperty("label.title"));
        grid.getColumn("author").setHeaderCaption("label.author");
        grid.getColumn("publishDate").setHeaderCaption("label.publish.date");
        grid.getColumn("style").setHeaderCaption("label.style");
        grid.getColumn("id").setHidden(true);
    }



    @Override
    public void enter(ViewChangeEvent event) {
    }
}