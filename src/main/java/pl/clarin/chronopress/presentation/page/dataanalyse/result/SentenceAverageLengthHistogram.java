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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.shered.SentenceQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.shered.dto.Unit;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class SentenceAverageLengthHistogram implements CalculationResult {

    private ChartPanel panel;

    @Inject
    private DbPropertiesProvider provider;

    private final Button downloadCSV = new Button(FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    @PostConstruct
    public void init() {

        downloadCSV.setCaption(provider.getProperty("label.download"));

        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.result.average.sentence.qt.panel.title"))
                .addTabSheet()
                .addChart(
                        provider.getProperty("label.result.sentence.qa.chart.title"),
                        provider.getProperty("label.result.sentence.qa.chart.x.axis.title"),
                        provider.getProperty("label.result.sentence.qa.chart.y.axis.title"),
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
        return "sentence_average";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(SentenceQuantitativeAnalysisResult data) {

        String unitValue = "";

        if (data.getSentence().getUnit() == Unit.WORD) {
            unitValue = provider.getProperty("label.word");
        }
        if (data.getSentence().getUnit() == Unit.LETTER) {
            unitValue = provider.getProperty("label.letter");
        }
        if (data.getSentence().getUnit() == Unit.SYLLABLE) {
            unitValue = provider.getProperty("label.syllable");
        }
        if (data.getSentence().getUnit() == Unit.FONEM) {
            unitValue = provider.getProperty("label.fonem");
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

        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException ex) {
            Logger.getLogger(SentenceAverageLengthHistogram.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileDownloader.extend(downloadCSV);
    }

    public Resource createExportContent(SentenceQuantitativeAnalysisResult data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file = java.io.File.createTempFile("sentenceAvrLength-" + date, ".csv");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            data.getSentence().getAverageLengthHistogram().forEach((k, v) -> {
                try {
                    writer.append(Long.toString(k) + ";" + Long.toString(v) + ";\n");
                } catch (IOException ex) {
                    Logger.getLogger(SentenceAverageLengthHistogram.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            writer.flush();
        }
        return new FileResource(file);
    }
}
