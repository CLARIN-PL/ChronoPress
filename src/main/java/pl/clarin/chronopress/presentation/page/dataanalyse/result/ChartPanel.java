package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesResult;

public class ChartPanel extends VerticalLayout {

    private final HorizontalLayout content;
    private final Chart chart;
    private final TabSheet sheet;

    private Queue<SolidColor> colors = initColorQueue();

    private ChartPanel(ChartPanelBuilder builder) {
        setCaption(builder.title);
        content = new MHorizontalLayout()
                .withFullWidth()
                .withSpacing(true);

        chart = builder.chart;
        sheet = builder.sheet;

        if (sheet != null) {
            content.addComponent(sheet);
        }
        if (chart != null) {
            content.addComponent(chart);
        }

        addComponent(content);
    }

    public void addDataWithDates(TimeSeriesResult result) {

        final Set<DataSeries> series = new HashSet<>();

        chart.getConfiguration().getxAxis().setType(AxisType.DATETIME);

        result.getTimeSeries().entrySet().forEach(e -> {
            final DataSeries s = new DataSeries(e.getKey());
            e.getValue().forEach(t -> {
                LocalDate ld = LocalDate.of(t.getYear(), t.getMonth() == 0 ? 1 : t.getMonth(), 1);
                s.add(new DataSeriesItem(Date.from(
                        ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                        t.getCount()));

            });
            series.add(s);
        });

        if (result.getMovingAverage() != null && result.getMovingAverage().size() > 0) {

            result.getMovingAverage().entrySet().forEach(e -> {
                final DataSeries s = new DataSeries(e.getKey() + "- średnia krocząca");
                e.getValue().forEach(t -> {
                    LocalDate ld = LocalDate.of(t.getYear(), t.getMonth() == 0 ? 1 : t.getMonth(), 1);
                    s.add(new DataSeriesItem(Date.from(
                            ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                            t.getCount()));

                });
                series.add(s);
            });
        }

        series.forEach(s -> chart.getConfiguration().addSeries(s));
        chart.getConfiguration().getChart().setZoomType(ZoomType.X);
        chart.drawChart();
    }

    public Configuration getConfiguration() {
        return chart.getConfiguration();
    }

    public void onlyGrid(String name, Map<Long, Long> map) {
        addDataSeries(chart, loadData(map, name));
    }
    
    public void gridWithTab(String name, FormBuilder form, Map<Long, Long> map) {
        String title = addTab(name, form);
        addDataSeries(chart, loadData(map, title));
    }

    private String addTab(final String name, final FormBuilder form) {
        String title = name + " - " + sheet.getComponentCount();
        form.setCaption(title);
        sheet.addTab(form);
        sheet.setSelectedTab(form);
        return title;
    }

    private void adjustXAxisMax(Long value) {
        chart.getConfiguration().getxAxis().setExtremes(1, value);
    }

    private Queue<SolidColor> initColorQueue() {
        Queue<SolidColor> q = new ArrayDeque<>();
        q.add(SolidColor.LIGHTBLUE);
        q.add(SolidColor.RED);
        q.add(SolidColor.GREEN);
        q.add(SolidColor.BLACK);
        q.add(SolidColor.BLUE);
        q.add(SolidColor.BROWN);
        q.add(SolidColor.VIOLET);
        q.add(SolidColor.GRAY);
        q.add(SolidColor.YELLOW);
        q.add(SolidColor.PINK);
        q.add(SolidColor.MAGENTA);
        q.add(SolidColor.AZURE);
        q.add(SolidColor.BEIGE);
        q.add(SolidColor.DARKGREEN);
        q.add(SolidColor.TOMATO);
        q.add(SolidColor.CYAN);
        q.add(SolidColor.DARKSALMON);
        q.add(SolidColor.FUCHSIA);
        q.add(SolidColor.LAVENDER);
        q.add(SolidColor.PLUM);
        return q;
    }

    private void addDataSeries(Chart chart, DataSeries data) {
        if (!colors.isEmpty()) {
            final PlotOptionsColumn opts = new PlotOptionsColumn();
            opts.setColor(colors.poll());
            opts.setStacking(Stacking.NORMAL);
            data.setPlotOptions(opts);
        }
        chart.getConfiguration().addSeries(data);
        chart.getConfiguration().getChart().setZoomType(ZoomType.X);
        chart.drawChart();
    }

    private DataSeries loadData(Map<? extends Number, ? extends Number> series, String name) {
        final DataSeries data = new DataSeries(name);
        series.forEach((x, y) -> data.add(new DataSeriesItem(x, y)));
        adjustXAxisMax((long) (series.size() + 10));
        return data;
    }
 
    public static class ChartPanelBuilder {

        private final String title;
        private Chart chart;
        private TabSheet sheet;

        public ChartPanelBuilder(String title) {
            this.title = title;
        }

        public ChartPanelBuilder addChart(String chartTitle, String xAxis, String yAxis, ChartType type) {
            chart = new Chart();
            chart.setLocale(new Locale("pl", "PL"));
            chart.setImmediate(true);
            chart.getConfiguration().setExporting(true);

            Configuration conf = chart.getConfiguration();
            conf.setTitle(chartTitle);
            conf.getChart().setType(type);

            conf.getxAxis().setTitle(xAxis);
            conf.getyAxis().setTitle(yAxis);
            return this;
        }

        public ChartPanelBuilder addTabSheet() {
            sheet = new TabSheet();
            return this;
        }

        public ChartPanel build() {
            return new ChartPanel(this);
        }
    }

    public static class FormBuilder extends VerticalLayout {

        final FormLayout form = new FormLayout();

        public FormBuilder() {
            setMargin(true);
            form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
            addComponent(form);
        }

        public FormBuilder addStringField(String caption, String value) {
            final TextField field = new TextField();
            field.setCaption(caption);
            field.setValue(value);
            form.addComponent(field);
            return this;
        }

        public FormBuilder addDoubleField(String caption, Double value) {
            final TextField field = new TextField();
            field.setCaption(caption);
            field.setValue(String.format("%.3f", value));
            form.addComponent(field);
            return this;
        }

        public FormBuilder build() {
            return this;
        }
    }

}
