package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.*;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.LexemeProfile;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class LexemeProfileList implements CalculationResult {

    private final HorizontalLayout panel = new HorizontalLayout();

    private final Grid grid = new Grid();

    private final Chart chart = new Chart(ChartType.PIE);

    @Inject
    private DbPropertiesProvider provider;

    private BeanItemContainer<LexemeProfile> container = new BeanItemContainer<>(LexemeProfile.class);

    private FileDownloader fileDownloader;
    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);

    @PostConstruct
    public void init() {

        initializeGrid();
        initChat();

        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setSpacing(true);

        HorizontalLayout download = new HorizontalLayout();
        download.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);
        download.addComponent(downloadCSV);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addComponent(grid);
        wrapper.addComponent(download);

        panel.addComponent(wrapper);
        panel.addComponent(chart);
        panel.setExpandRatio(wrapper, 2);
        panel.setExpandRatio(chart, 1);
        panel.setCaption(provider.getProperty("label.lexeme.profile.list"));

    }

    private void initChat() {
        chart.setLocale(new Locale("pl", "PL"));
        chart.setImmediate(true);
        chart.getConfiguration().setExporting(true);

        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize("0");
        chart.getConfiguration().setPlotOptions(options);

        chart.getConfiguration().setTitle("Profil semantyczny");//provider.getProperty("view.data.exploration.profile.chart.title"));

    }

    @Override
    public String getType() {
        return "profile";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    private void addChartData(List<LexemeProfile> data) {
        Collections.reverse(data);
        DataSeries series = new DataSeries();
        final int[] counter = {0};
        data.forEach(d -> {
            if (counter[0] < 10) {
                series.add(new DataSeriesItem(d.getBaseColocat(), d.getPercentage()));
                counter[0]++;
            }
        });
        chart.getConfiguration().addSeries(series);
    }

    public void addData(List<LexemeProfile> data, String lemma) {
        container.removeAllItems();
        container.addAll(data);
        addChartData(data);
        grid.sort("count", SortDirection.DESCENDING);
        chart.getConfiguration().setSubTitle("leksem '[" + lemma + "]'");
        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("baseColocat", "match", "count", "percentage");
        grid.getColumn("baseColocat").setHeaderCaption("Kolokat");
        grid.getColumn("match").setHeaderCaption("Współwystąpienia");
        grid.getColumn("count").setHeaderCaption("Częstość");
        grid.getColumn("percentage").setHeaderCaption(provider.getProperty("label.profile.percentage"));

    }

    public Resource createExportContent(List<LexemeProfile> data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file = java.io.File.createTempFile("profile-list-" + date, ".csv");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(3);
            data.forEach(i -> {
                try {
                    writer.append(i.getBaseColocat() + ";" + i.getMatch() + ";" + i.getCount() + ";" + df.format(i.getPercentage()) + ";\n");
                } catch (IOException e) {
                    log.debug("Export to csv", e);
                }
            });
            writer.flush();
        }
        return new FileResource(file);
    }
}
