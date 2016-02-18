package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;


public class ConcordanceList implements CalculationResult {

    private final VerticalLayout panel = new VerticalLayout();

    private final Grid grid = new Grid();

    private DbPropertiesProvider provider;

    private BeanItemContainer<ConcordanceDTO> container = new BeanItemContainer<>(ConcordanceDTO.class);

    public ConcordanceList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setMargin(true);
        panel.addComponent(grid);
        panel.setCaption(provider.getProperty("label.lexeme.concordance.list"));
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(List<ConcordanceDTO> data) {
        container.removeAllItems();
        container.addAll(data);
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("base", "lemma", "sentence", "publicationDate", "journalTitle");
    }

}
