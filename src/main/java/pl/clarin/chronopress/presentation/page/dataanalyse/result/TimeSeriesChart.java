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
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesResult;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class TimeSeriesChart implements CalculationResult {

    private ChartPanel panel;

    @Inject
    private DbPropertiesProvider provider;

    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    @PostConstruct
    public void init() {
        
        panel = new ChartPanel.ChartPanelBuilder(provider.getProperty("label.time.series.panel.title"))
                .addChart(provider.getProperty("label.time.series.chart.title"),
                        provider.getProperty("label.time.series.x.axis.title"),
                        provider.getProperty("label.time.series.y.axis.title"),
                        ChartType.LINE)
                .build();

        HorizontalLayout download = new HorizontalLayout();
        download.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);
        download.addComponent(downloadCSV);
        panel.addComponent(download);
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
        panel.addDataWithDates(data);
        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to CSV", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    public Resource createExportContent(TimeSeriesResult data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file =  java.io.File.createTempFile("timeSeries-"+date , ".csv");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            data.getTimeSeries().forEach((k, v) -> {
                try {
                    writer.append(k +"\n");
                    writer.append("month\t"+"year\t"+"frequency\t\n");
                    v.forEach(tp -> {
                        try {
                            writer.append(tp.getMonth() + ";" + tp.getYear() +";"+ tp.getCount() + ";\n");
                        } catch (IOException e) {
                            log.debug("Export to CSV", e);
                        }
                    });
                } catch (IOException e) {
                    log.debug("Export to CSV", e);
                }
            });
            writer.flush();
        }
        return new FileResource(file);
    }
}
