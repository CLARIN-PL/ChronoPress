package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.ui.Component;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;


public class WordZipfFrequencyHistogram implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    public WordZipfFrequencyHistogram(DbPropertiesProvider provider) {
        this.provider = provider;
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.frequency.word.qt.panel.title"))
                .addChart(provider.getProperty("label.frequency.word.qa.chart.title"),
                        provider.getProperty("label.frequency.word.qa.chart.x.axis.title"),
                        provider.getProperty("label.frequency.word.qa.chart.y.axis.title"),
                        ChartType.COLUMN)
                .build();
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(QuantitativeAnalysisResult data) {
        panel.onlyGrid("", data.getWordFrequencyHistogram());
    }
}
