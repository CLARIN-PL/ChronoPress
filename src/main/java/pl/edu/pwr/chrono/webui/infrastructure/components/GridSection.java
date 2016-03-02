package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 01.01.16.
 */

@SpringComponent
@Scope("prototype")
public class GridSection extends VerticalLayout {

    private Grid grid;
    private VerticalLayout loadingNotification;
    private VerticalLayout noResults;

    @Autowired
    private DbPropertiesProvider properties;

    @PostConstruct
    public void init() {
        setSizeFull();
        grid = new Grid();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setReadOnly(true);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(20);
        grid.setEditorSaveCaption(properties.getProperty("button.save"));
        grid.setEditorCancelCaption(properties.getProperty("button.cancel"));
        loadingNotification = addLoadingNotification();
        noResults = addNoResults();
    }

    public Grid getGrid() {
        return grid;
    }

    public void showGrid() {
        removeAllComponents();
        addComponent(grid);
    }

    public void showLoading() {
        removeAllComponents();
        addComponent(loadingNotification);
    }

    public void showNoResults() {
        removeAllComponents();
        addComponent(noResults);
    }

    private VerticalLayout addLoadingNotification() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);

        HorizontalLayout loadingNotification = new HorizontalLayout();
        loadingNotification.setSpacing(true);
        loadingNotification.addComponents(progressBar, new Label(properties.getProperty("label.loading")));

        layout.addComponents(loadingNotification);
        layout.setComponentAlignment(loadingNotification, Alignment.MIDDLE_CENTER);

        return layout;
    }

    private VerticalLayout addNoResults() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        Label label = new Label(properties.getProperty("label.no.results.found"));

        layout.addComponent(label);
        layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        return layout;
    }
}
