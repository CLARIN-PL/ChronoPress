package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChartPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 08.02.16.
 */

@SpringComponent
@ViewScope
public class QuantitativeAnalysisTab extends VerticalLayout {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuantitativeAnalysisTab.class);

    @Autowired
    private DbPropertiesProvider provider;

    @PropertyId("allPartsOfSpeech")
    private final CheckBox allPartsOfSpeech = new CheckBox();

    @PropertyId("adjective")
    private final CheckBox adjective = new CheckBox();

    @PropertyId("verb")
    private final CheckBox verb = new CheckBox();

    @PropertyId("adverb")
    private final CheckBox adverb = new CheckBox();

    @PropertyId("noun")
    private final CheckBox noun = new CheckBox();

    @PropertyId("namingUnit")
    private final CheckBox namingUnit = new CheckBox();

    @PropertyId("regularExpression")
    private final CheckBox regularExpression = new CheckBox();

    @PropertyId("expression")
    private final TextField expression = new TextField();

    @PropertyId("wordLetterUnit")
    private final CheckBox wordLetterUnit = new CheckBox();

    @PropertyId("wordSyllableUnit")
    private final CheckBox wordSyllableUnit = new CheckBox();

    @PropertyId("wordAveragesLength")
    private final CheckBox wordAveragesLength = new CheckBox();

    @PropertyId("wordStandardDeviation")
    private final CheckBox wordStandardDeviation = new CheckBox();

    @PropertyId("wordCoefficientOfVariation")
    private final CheckBox wordCoefficientOfVariation = new CheckBox();

    @PropertyId("wordSkewness")
    private final CheckBox wordSkewness = new CheckBox();

    @PropertyId("wordKurtosis")
    private final CheckBox wordKurtosis = new CheckBox();

    @PropertyId("wordEmpiricalDistributionZipfHistogram")
    private final CheckBox wordEmpiricalDistributionZipfHistogram = new CheckBox();

    @PropertyId("sentenceWordUnit")
    private final CheckBox sentenceWordUnit = new CheckBox();

    @PropertyId("sentenceLetterUnit")
    private final CheckBox sentenceLetterUnit = new CheckBox();

    @PropertyId("sentenceAveragesLength")
    private final CheckBox sentenceAveragesLength = new CheckBox();

    @PropertyId("sentenceStandardDeviation")
    private final CheckBox sentenceStandardDeviation = new CheckBox();

    @PropertyId("sentenceCoefficientOfVariation")
    private final CheckBox sentenceCoefficientOfVariation = new CheckBox();

    @PropertyId("sentenceSkewness")
    private final CheckBox sentenceSkewness = new CheckBox();

    @PropertyId("sentenceEmpiricalDistributionLength")
    private final CheckBox sentenceEmpiricalDistributionLength = new CheckBox();

    private final BeanFieldGroup<QuantitativeAnalysisDTO> binder = new BeanFieldGroup<>(QuantitativeAnalysisDTO.class);

    private final Button clear = new Button();
    private final Button accept = new Button();

    @Autowired
    private ChartPanel resultChartPanel;

    @PostConstruct
    public void init() {
        setMargin(true);
        setCaption(provider.getProperty("view.tab.quantitative.analysis.title"));
        setSpacing(true);

        HorizontalLayout panels = initializePanels();
        addComponent(panels);
        setComponentAlignment(panels, Alignment.MIDDLE_CENTER);

        HorizontalLayout buttons = initButtons();
        addComponent(buttons);
        setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        binder.setItemDataSource(new QuantitativeAnalysisDTO());
        binder.bindMemberFields(this);
        expression.setVisible(false);
    }

    public void addData(QuantitativeAnalysisResult data){
        removeComponent(resultChartPanel);
        resultChartPanel.addWordData(data);
        resultChartPanel.addSentenceData(data);
        addComponent(resultChartPanel);
    }

    private HorizontalLayout initButtons(){

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        clear.setCaption(provider.getProperty("label.clear"));
        accept.setCaption(provider.getProperty("label.accept"));

        layout.addComponent(clear);
        layout.addComponent(accept);
        return layout;
    }

    public QuantitativeAnalysisDTO getQuantitativeAnalysisDTO(){
        try {
            binder.commit();
        } catch (FieldGroup.CommitException e) {
            LOGGER.info("Commit failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    private HorizontalLayout initializePanels(){

        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        Panel filter = new Panel();
        filter.setCaption(provider.getProperty("panel.filter.title"));
        filter.addStyleName(ValoTheme.PANEL_BORDERLESS);
        filter.addStyleName(ChronoTheme.RESULT_PANEL);
        filter.setContent(initializeFilterColumn());

        Panel words = new Panel();
        words.setCaption(provider.getProperty("panel.words.title."));
        words.addStyleName(ValoTheme.PANEL_BORDERLESS);
        words.addStyleName(ChronoTheme.RESULT_PANEL);
        words.setContent(initializeWordColumn());

        Panel sentence = new Panel();
        sentence.setCaption(provider.getProperty("panel.sentence.title."));
        sentence.addStyleName(ValoTheme.PANEL_BORDERLESS);
        sentence.addStyleName(ChronoTheme.RESULT_PANEL);
        sentence.setContent(initializeSentenceColumn());

        layout.addComponent(filter);
        layout.addComponent(words);
        layout.addComponent(sentence);

        return layout;
    }

    private VerticalLayout initializeFilterColumn(){
        final VerticalLayout column = new VerticalLayout();
        column.setMargin(true);

        initCheckBox(provider.getProperty("label.all.parts.of.speech"),allPartsOfSpeech, column);
        initCheckBox(provider.getProperty("label.adjective"), adjective, column);
        initCheckBox(provider.getProperty("label.verb"), verb, column);
        initCheckBox(provider.getProperty("label.adverb"), adverb, column);
        initCheckBox(provider.getProperty("label.noun"), noun, column);
        initCheckBox(provider.getProperty("label.namingUnit"), namingUnit, column);
        initCheckBox(provider.getProperty("label.regular.expression"), regularExpression, column);

        regularExpression.addValueChangeListener(event -> {
            expression.setVisible(!expression.isVisible());
        });

        expression.setCaption(provider.getProperty("label.expression"));
        expression.addStyleName(ValoTheme.TEXTFIELD_TINY);
        expression.setNullRepresentation("");
        column.addComponent(expression);

        return column;
    }

    private void initCheckBox(String caption, CheckBox box, final AbstractOrderedLayout column){
        box.setCaption(caption);
        box.addStyleName(ValoTheme.CHECKBOX_SMALL);
        column.addComponent(box);
    }

    private VerticalLayout initializeWordColumn() {
        final VerticalLayout column = new VerticalLayout();
        column.setMargin(true);

        HorizontalLayout unit = new HorizontalLayout();
        unit.setSpacing(true);
        Label desc = new Label(provider.getProperty("label.unit"));
        desc.addStyleName(ValoTheme.LABEL_SMALL);
        unit.addComponent(desc);

        initCheckBox(provider.getProperty("label.letter"), wordLetterUnit, unit);
        initCheckBox(provider.getProperty("label.syllable"), wordSyllableUnit, unit);

        column.addComponent(unit);

        initCheckBox(provider.getProperty("label.average.length"), wordAveragesLength,column);
        initCheckBox(provider.getProperty("label.standard.deviation"), wordStandardDeviation, column);
        initCheckBox(provider.getProperty("label.coefficient.of.variation"),  wordCoefficientOfVariation, column);
        initCheckBox(provider.getProperty("label.skewness"), wordSkewness, column );
        initCheckBox(provider.getProperty("label.kurtosis"), wordKurtosis, column );
        initCheckBox(provider.getProperty("label.empirical.distribution.zipf.histogram"),  wordEmpiricalDistributionZipfHistogram, column);
        return  column;
    }

    private VerticalLayout initializeSentenceColumn() {
        final VerticalLayout column = new VerticalLayout();
        column.setMargin(true);

        HorizontalLayout unit = new HorizontalLayout();
        unit.setSpacing(true);
        Label desc = new Label(provider.getProperty("label.unit"));
        desc.addStyleName(ValoTheme.LABEL_SMALL);
        unit.addComponent(desc);

        initCheckBox(provider.getProperty("label.word"),  sentenceWordUnit, unit);
        initCheckBox(provider.getProperty("label.letter"), sentenceLetterUnit, unit);
        column.addComponent(unit);

        initCheckBox(provider.getProperty("label.average.length"),  sentenceAveragesLength, column);
        initCheckBox(provider.getProperty("label.standard.deviation"), sentenceStandardDeviation, column);
        initCheckBox(provider.getProperty("label.coefficient.of.variation"), sentenceCoefficientOfVariation, column);
        initCheckBox(provider.getProperty("label.skewness"), sentenceSkewness, column);
        initCheckBox(provider.getProperty("label.empirical.distribution.length"), sentenceEmpiricalDistributionLength, column);


        return  column;
    }

    public Button getClear() {
        return clear;
    }

    public Button getAccept() {
        return accept;
    }
}
