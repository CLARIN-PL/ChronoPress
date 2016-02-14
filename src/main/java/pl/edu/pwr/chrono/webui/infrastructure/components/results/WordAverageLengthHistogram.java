package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.ui.Component;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

public class WordAverageLengthHistogram implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    public WordAverageLengthHistogram(DbPropertiesProvider provider) {
        this.provider = provider;
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.average..word.qt.panel.title"))
                .addTabSheet()
                .addChart(provider.getProperty("label.result.word.qa.chart.title"),
                        provider.getProperty("label.result.word.qa.chart.x.axis.title"),
                        provider.getProperty("label.result.word.qa.chart.y.axis.title"),
                        ChartType.COLUMN)
                .build();
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(QuantitativeAnalysisResult data) {

        String unitValue;

        if (data.getWord().getUnit() == Unit.LETTER) {
            unitValue = provider.getProperty("label.unit.letter");
        } else {
            unitValue = provider.getProperty("label.unit.syllable");
        }

        panel.gridWithTab(
                provider.getProperty("label.sample"),
                new ChartPanel.FormBuilder()
                        .addStringField(provider.getProperty("label.unit"), unitValue)
                        .addDoubleField(provider.getProperty("label.average.length"), data.getWord().getAveragesLength())
                        .addDoubleField(provider.getProperty("label.standard.deviation"), data.getWord().getStandardDeviation())
                        .addDoubleField(provider.getProperty("label.coefficient"), data.getWord().getCoefficientOfVariation())
                        .addDoubleField(provider.getProperty("label.skewness"), data.getWord().getSkewness())
                        .addDoubleField(provider.getProperty("label.kurtosis"), data.getWord().getKurtosis())
                        .build(),
                data.getWord().getAverageLengthHistogram());
    }
}
