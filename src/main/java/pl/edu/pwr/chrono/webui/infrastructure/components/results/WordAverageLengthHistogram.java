package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class WordAverageLengthHistogram implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    public WordAverageLengthHistogram(DbPropertiesProvider provider) {
        this.provider = provider;
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.average.word.qt.panel.title"))
                .addTabSheet()
                .addChart(provider.getProperty("label.result.word.qa.chart.title"),
                        provider.getProperty("label.result.word.qa.chart.x.axis.title"),
                        provider.getProperty("label.result.word.qa.chart.y.axis.title"),
                        ChartType.COLUMN)
                .build();

        HorizontalLayout download = new HorizontalLayout();
        download.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);
        download.addComponent(downloadCSV);

        panel.addComponent(download);
    }

    @Override
    public String getType() {
        return "word_average";
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

        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    public Resource createExportContent(QuantitativeAnalysisResult data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file =  java.io.File.createTempFile("wordAvrLength-"+date , ".csv");
        file.deleteOnExit();
            FileWriter writer = new FileWriter(file);
            data.getWord().getAverageLengthHistogram().forEach((k, v) -> {
                try {
                    writer.append(Long.toString(k) + ";" + Long.toString(v) + ";\n");
                } catch (IOException e) {
                    log.debug("Export to CSV", e);
                }
            });
        writer.flush();
           writer.close();
         return new FileResource(file);
    }
}
