package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.ui.Component;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

public class TimeSeriesChart implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    public TimeSeriesChart(DbPropertiesProvider provider) {
        this.provider = provider;
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.time.series.panel.title"))
                .addChart(provider.getProperty("label.time.series.chart.title"),
                        provider.getProperty("label.time.series.x.axis.title"),
                        provider.getProperty("label.time.series.y.axis.title"),
                        ChartType.LINE)
                .build();
    }

    @Override
    public String getType() {
        return "timeSeries";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(TimeSeriesResult data) {
        panel.addDataWithDates(data, provider);
    }
}
