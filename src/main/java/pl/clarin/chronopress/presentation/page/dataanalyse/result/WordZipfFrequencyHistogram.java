package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.shered.WordQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class WordZipfFrequencyHistogram implements CalculationResult {

    private ChartPanel panel;

    @Inject
    DbPropertiesProvider provider;

    private final Button downloadCSV = new Button(FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    @PostConstruct
    public void init() {
        downloadCSV.setCaption(provider.getProperty("label.download"));

        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.frequency.word.qt.panel.title"))
                .addChart(provider.getProperty("label.word.zipfa.histogram"),
                        provider.getProperty("label.frequency.word.zipf.chart.x.axis.title"),
                        provider.getProperty("label.frequency.word.zipf.chart.y.axis.title"),
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

    public void addData(WordQuantitativeAnalysisResult data) {
        panel.onlyGrid(provider.getProperty("label.sample"), data.getWordFrequencyHistogram());
        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    public Resource createExportContent(WordQuantitativeAnalysisResult data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file = java.io.File.createTempFile("wordZipfFreque-" + date, ".csv");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            data.getWordFrequencyHistogram().forEach((k, v) -> {
                try {
                    writer.append(Long.toString(k) + ";" + Long.toString(v) + ";\n");
                } catch (IOException e) {
                    log.debug("Export to CSV", e);
                }
            });
            writer.flush();
        }
        return new FileResource(file);
    }
}
