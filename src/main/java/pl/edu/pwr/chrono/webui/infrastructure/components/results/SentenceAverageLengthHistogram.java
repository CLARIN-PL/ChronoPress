package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.ui.Component;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

public class SentenceAverageLengthHistogram implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    public SentenceAverageLengthHistogram(DbPropertiesProvider provider) {
        this.provider = provider;

        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.average.sentence.qt.panel.title"))
                .addTabSheet()
                .addChart(provider.getProperty("label.result.sentence.qa.chart.title"),
                        provider.getProperty("label.result.sentence.qa.chart.x.axis.title"),
                        provider.getProperty("label.result.sentence.qa.chart.y.axis.title"),
                        ChartType.COLUMN)
                .build();
    }

    @Override
    public String getType() {
        return "sentence_average";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(QuantitativeAnalysisResult data) {
        String unitValue;

        if (data.getSentence().getUnit() == Unit.LETTER) {
            unitValue = provider.getProperty("label.unit.letter");
        } else {
            unitValue = provider.getProperty("label.unit.word");
        }
        panel.gridWithTab(
                provider.getProperty("label.sample"),
                new ChartPanel.FormBuilder()
                        .addStringField(provider.getProperty("label.unit"), unitValue)
                        .addDoubleField(provider.getProperty("label.average.length"), data.getSentence().getAveragesLength())
                        .addDoubleField(provider.getProperty("label.standard.deviation"), data.getSentence().getStandardDeviation())
                        .addDoubleField(provider.getProperty("label.coefficient"), data.getSentence().getCoefficientOfVariation())
                        .addDoubleField(provider.getProperty("label.skewness"), data.getSentence().getSkewness())
                        .build(),
                data.getSentence().getAverageLengthHistogram());

    }
}
