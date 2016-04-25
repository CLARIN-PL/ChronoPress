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
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class WordZipfFrequencyHistogram implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;
    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    public WordZipfFrequencyHistogram(DbPropertiesProvider provider) {
        this.provider = provider;
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.frequency.word.qt.panel.title"))
                .addChart(provider.getProperty("label.frequency.word.qa.chart.title"),
                        provider.getProperty("label.frequency.word.qa.chart.x.axis.title"),
                        provider.getProperty("label.frequency.word.qa.chart.y.axis.title"),
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
        return "word_zipf";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(QuantitativeAnalysisResult data) {
        panel.onlyGrid(provider.getProperty("label.sample"), data.getWordFrequencyHistogram());
        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    public Resource createExportContent(QuantitativeAnalysisResult data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file = java.io.File.createTempFile("wordZipfFreque-"+date , ".csv");
            file.deleteOnExit();
            FileWriter writer = new FileWriter(file);
            data.getWordFrequencyHistogram().forEach((k, v) -> {
                try {
                    writer.append(Long.toString(k) + "\t" + Long.toString(v) + "\t\n");
                } catch (IOException e) {
                    log.debug("Export to CSV" , e);
                }
            });
        writer.flush();
            writer.close();
        return new FileResource(file);
    }
}
