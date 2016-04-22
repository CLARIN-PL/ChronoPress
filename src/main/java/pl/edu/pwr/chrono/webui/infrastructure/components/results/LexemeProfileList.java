package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import pl.edu.pwr.chrono.readmodel.dto.LexemeProfile;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;
import java.util.Locale;


public class LexemeProfileList implements CalculationResult {

    private final HorizontalLayout panel = new HorizontalLayout();

    private final Grid grid = new Grid();

    private final Chart chart = new Chart(ChartType.PIE);

    private DbPropertiesProvider provider;

    private BeanItemContainer<LexemeProfile> container = new BeanItemContainer<>(LexemeProfile.class);

    public LexemeProfileList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        initChat();

        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setSpacing(true);

        panel.addComponent(grid);
        panel.addComponent(chart);
        panel.setExpandRatio(grid, 2);
        panel.setExpandRatio(chart, 1);
        panel.setCaption(provider.getProperty("label.lexeme.profile.list"));
    }

    private void initChat(){
        chart.setLocale(new Locale("pl", "PL"));
        chart.setImmediate(true);

        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize("0");
        chart.getConfiguration().setPlotOptions(options);
        chart.getConfiguration().setTitle(provider.getProperty("view.data.exploration.profile.chart.title"));
    }

    @Override
    public String getType() {
        return "profile";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    private void addChartData(List<LexemeProfile> data){
        DataSeries series = new DataSeries();
        data.forEach(d -> series.add(new DataSeriesItem(d.getBaseColocat(), d.getPercentage())));
        chart.getConfiguration().addSeries(series);
    }

    public void addData(List<LexemeProfile> data) {
        container.removeAllItems();
        container.addAll(data);
        addChartData(data);
        grid.sort("count", SortDirection.DESCENDING);
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("baseColocat", "match", "count");
        grid.getColumn("baseColocat").setHeaderCaption(provider.getProperty("label.colocat"));
        grid.getColumn("match").setHeaderCaption(provider.getProperty("label.match.word"));
        grid.getColumn("count").setHeaderCaption(provider.getProperty("label.frequency.count"));

    }
}
