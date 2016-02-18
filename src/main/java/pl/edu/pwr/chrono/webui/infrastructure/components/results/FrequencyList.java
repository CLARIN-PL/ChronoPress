package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import pl.edu.pwr.chrono.readmodel.dto.WordFrequencyDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;


public class FrequencyList implements CalculationResult {

    private final VerticalLayout panel = new VerticalLayout();

    private final Grid grid = new Grid();

    private DbPropertiesProvider provider;

    private BeanItemContainer<WordFrequencyDTO> container = new BeanItemContainer<WordFrequencyDTO>(WordFrequencyDTO.class);

    public FrequencyList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setMargin(true);
        panel.addComponent(grid);
        panel.setCaption(provider.getProperty("label.lexeme.frequency.list"));
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(List<WordFrequencyDTO> data) {
        container.removeAllItems();
        container.addAll(data);
        grid.sort("count", SortDirection.DESCENDING);
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("word", "partOfSpeech", "count", "percentage");
        grid.getColumn("word").setHeaderCaption(provider.getProperty("label.word"));
        grid.getColumn("partOfSpeech").setHeaderCaption(provider.getProperty("label.part.of.speech"));
        grid.getColumn("count").setHeaderCaption(provider.getProperty("label.frequency.count"));
        grid.getColumn("percentage").setHeaderCaption(provider.getProperty("label.percentage"));
    }

}
