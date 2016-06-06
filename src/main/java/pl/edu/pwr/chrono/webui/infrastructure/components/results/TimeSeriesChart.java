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
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
@Slf4j
public class TimeSeriesChart implements CalculationResult {

    private final ChartPanel panel;

    private DbPropertiesProvider provider;

    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    public TimeSeriesChart(DbPropertiesProvider provider) {
        this.provider = provider;
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
        panel.addDataWithDates(data, provider);
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
        FileWriter writer = new FileWriter(file);
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
        writer.close();
        return new FileResource(file);
    }
}
