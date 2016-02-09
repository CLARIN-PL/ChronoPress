package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by tnaskret on 09.02.16.
 */

@SpringComponent
@ViewScope
public class ChartPanel extends Panel {

    @Autowired
    private DbPropertiesProvider provider;

    private final TabSheet wordTabSheet = new TabSheet();
    private final Chart wordChart = new Chart();
    private final Chart sentenceChart = new Chart();
    private final TabSheet sentenceTabSheet = new TabSheet();

    private VerticalLayout wordResultColumn;
    private VerticalLayout sentenceResultColumn;

    private int wordTabCounter = 1;
    private int sentenceTabCounter = 1;

    @PostConstruct
    public void init(){
        setCaption(provider.getProperty("label.results"));
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        addStyleName(ChronoTheme.RESULT_PANEL);

        final HorizontalLayout content = new HorizontalLayout();
        setContent(content);
        initWordChart();

        wordResultColumn = initContentColumnLayout(wordTabSheet, wordChart);
        sentenceResultColumn = initContentColumnLayout(sentenceTabSheet, sentenceChart);

        content.addComponent(wordResultColumn);
        content.addComponent(sentenceResultColumn);
    }

    public void addWordData(QuantitativeAnalysisResult result){
        String series =  provider.getProperty("label.sample") +" - "+ wordTabCounter++;
        wordTabSheet.addTab(buildForm(series, result));
        addDataSeries(wordChart, series, result);
    }

    public void addSentenceData(QuantitativeAnalysisResult result){
        String series =  provider.getProperty("label.sample") +" - "+ sentenceTabCounter++;
        sentenceTabSheet.addTab(buildForm(series, result));
        addDataSeries(sentenceChart, series, result);
    }

    private VerticalLayout initContentColumnLayout(TabSheet sheet, Chart chart){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(sheet);
        layout.addComponent(chart);
        return layout;
    }

    private  void initWordChart(){
        wordChart.setImmediate(true);
        Configuration conf = wordChart.getConfiguration();
        conf.setTitle(provider.getProperty("label.word.histogram.title"));
        conf.getChart().setType(ChartType.COLUMN);

        conf.getxAxis().setTitle(provider.getProperty("label.word.length"));
        conf.getyAxis().setTitle(provider.getProperty("label.frequency.count"));

    }

    private VerticalLayout buildForm(String name, QuantitativeAnalysisResult result){

        final VerticalLayout wrapper = new VerticalLayout();
        wrapper.setCaption(name);
        wrapper.setMargin(new MarginInfo(false, true, false, true));

        final TextField unit = new TextField();
        unit.setCaption(provider.getProperty("label.unit"));

        if(result.getWordLetterUnit()) {
            unit.setValue(provider.getProperty("label.unit.letter"));
        }
        if(result.getWordSyllableUnit()) {
            unit.setValue(provider.getProperty("label.unit.syllable"));
        }

        final TextField wordAverageLength = new TextField();
        wordAverageLength.setCaption(provider.getProperty("label.average.length"));
        wordAverageLength.setValue(Double.toString(result.getWordAveragesLength()));

        final TextField wordCoefficientOfVariation = new TextField();
        wordCoefficientOfVariation.setCaption(provider.getProperty("label.coefficient"));
        wordCoefficientOfVariation.setValue(Double.toString(result.getWordCoefficientOfVariation()));

        final TextField wordStandardDeviation = new TextField();
        wordStandardDeviation.setCaption(provider.getProperty("label.standard.deviation"));
        wordStandardDeviation.setValue(Double.toString(result.getWordStandardDeviation()));

        final TextField wordSkewness = new TextField();
        wordSkewness.setCaption(provider.getProperty("label.skewness"));
        wordSkewness.setValue(Double.toString(result.getWordSkewness()));

        final TextField wordKurtosis = new TextField();
        wordKurtosis.setCaption(provider.getProperty("label.kurtosis"));
        wordKurtosis.setValue(Double.toString(result.getWordKurtosis()));

        final FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        form.addComponent(unit);
        form.addComponent(wordAverageLength);
        form.addComponent(wordCoefficientOfVariation);
        form.addComponent(wordStandardDeviation);
        form.addComponent(wordSkewness);
        form.addComponent(wordKurtosis);
        wrapper.addComponent(form);

        return wrapper;
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


    private void addDataSeries(Chart chart, String name, QuantitativeAnalysisResult result){

        final DataSeries data = new DataSeries(name);

        if(!colors.isEmpty()) {
            final PlotOptionsColumn opts = new PlotOptionsColumn();;
            opts.setColor(colors.poll());
            opts.setStacking(Stacking.NORMAL);
            data.setPlotOptions(opts);
        }
        result.getWordLengthFrequency().forEach((x, y) -> {
            data.add(new DataSeriesItem(x, y));
        });
        chart.getConfiguration().addSeries(data);
        chart.drawChart();
    }
}
