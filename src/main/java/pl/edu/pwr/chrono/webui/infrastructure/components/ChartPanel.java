package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

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

    public void addData(String name, FormBuilder form, Map<Integer, Long> map){
        String title =  name +" - "+ tabCounter++;
        sheet.addTab(form);
        addDataSeries(chart, map, title);
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

    public static class FormBuilder extends VerticalLayout{

        final FormLayout form = new FormLayout();

        public FormBuilder(String name){
            setCaption(name);
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

    private void addDataSeries(Chart chart, Map<Integer, Long> series, String name){

        final DataSeries data = new DataSeries(name);

        if(!colors.isEmpty()) {
            final PlotOptionsColumn opts = new PlotOptionsColumn();;
            opts.setColor(colors.poll());
            opts.setStacking(Stacking.NORMAL);
            data.setPlotOptions(opts);
        }
        series.forEach((x, y) -> {
            data.add(new DataSeriesItem(x, y));
        });
        chart.getConfiguration().addSeries(data);
        chart.drawChart();
    }
}
