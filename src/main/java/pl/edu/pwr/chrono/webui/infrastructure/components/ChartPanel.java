package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by tnaskret on 09.02.16.
 */

public class ChartPanel extends VerticalLayout {

    private final TabSheet sheet = new TabSheet();
    private final Chart chart = new Chart();
    private int tabCounter = 1;


    public ChartPanel(String title){
        setCaption(title);
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        addStyleName(ChronoTheme.RESULT_TITLE);
        addComponent(initContentColumnLayout(sheet, chart));
    }

    public void reset(){
        tabCounter = 1;
        sheet.removeAllComponents();
        chart.getConfiguration().getSeries().clear();
    }

    public void addDataWithInteger(String name, FormBuilder form, Map<Integer, Long> map){
        String title = buildTab(name, form);
        addDataSeries(chart, loadIntegerData(map, title));
    }

    private String buildTab(String name, FormBuilder form) {
        String title =  name +" - "+ tabCounter++;
        form.setCaption(title);
        sheet.addTab(form);
        sheet.setSelectedTab(form);
        return title;
    }

    public void addDataWithLong(String name, FormBuilder form, Map<Long, Long> map){
        String title = buildTab(name, form);
        addDataSeries(chart, loadLongData(map, title));
    }

    private HorizontalLayout initContentColumnLayout(TabSheet sheet, Chart chart){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setSpacing(true);
        layout.addComponent(sheet);
        layout.addComponent(chart);
        return layout;
    }

    public  void initChart(String chartTitle, String xAxis, String yAxis){
        chart.setImmediate(true);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(chartTitle);
        conf.getChart().setType(ChartType.COLUMN);

        conf.getxAxis().setTitle(xAxis);
        conf.getyAxis().setTitle(yAxis);

    }

    private void adjustXAxisMax(Long value) {
        chart.getConfiguration().getxAxis().setExtremes(1, value);
    }

    public static class FormBuilder extends VerticalLayout{

        final FormLayout form = new FormLayout();

        public FormBuilder(){
            setMargin(true);
            form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
            addComponent(form);
        }

        public FormBuilder addStringField(String caption, String value){
            final TextField field = new TextField();
            field.setCaption(caption);
            field.setValue(value);
            form.addComponent(field);
            return this;
        }

        public FormBuilder addDoubleField(String caption, Double value){
            final TextField field = new TextField();
            field.setCaption(caption);
            field.setValue(Double.toString(value));
            form.addComponent(field);
            return this;
        }

        public FormBuilder build(){
            return this;
        }
    }

    private Queue<SolidColor> colors = initColorQueue();

    private Queue<SolidColor> initColorQueue(){
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
        return q;
    }

    private void addDataSeries(Chart chart,  DataSeries data){
        if(!colors.isEmpty()) {
            final PlotOptionsColumn opts = new PlotOptionsColumn();
            opts.setColor(colors.poll());
            opts.setStacking(Stacking.NORMAL);
            data.setPlotOptions(opts);
        }
        chart.getConfiguration().addSeries(data);
        chart.getConfiguration().getChart().setZoomType(ZoomType.X);
        chart.drawChart();
    }

    private  DataSeries loadLongData(Map<Long, Long> series, String name) {
        final DataSeries data = new DataSeries(name);
        series.forEach((x, y) -> {
            data.add(new DataSeriesItem(x, y));
        });
        adjustXAxisMax(Long.valueOf(series.size() +10));
        return data;
    }

    private  DataSeries loadIntegerData(Map<Integer, Long> series, String name) {
        final DataSeries data = new DataSeries(name);
        series.forEach((x, y) -> {
            data.add(new DataSeriesItem(x, y));
        });
        return  data;
    }
}
