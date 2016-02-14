package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.MultiColumnPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.Tab;

@SpringComponent
@ViewScope
@Slf4j
public class QuantitativeAnalysisTab extends Tab {

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

    @PropertyId("wordRegularExpression")
    private final TextField wordRegularExpression = new TextField();

    @PropertyId("sentenceRegularExpression")
    private final TextField sentenceRegularExpression = new TextField();

    @PropertyId("wordUnit")
    private final OptionGroup wordUnit = new OptionGroup();

    @PropertyId("wordAveragesLengthHistogram")
    private final CheckBox wordAveragesLengthHistogram = new CheckBox();

    @PropertyId("wordZipfHistogram")
    private final CheckBox wordZipfHistogram = new CheckBox();

    @PropertyId("sentenceUnit")
    private final OptionGroup sentenceUnit = new OptionGroup();

    @PropertyId("sentenceAverageLengthHistogram")
    private final CheckBox sentenceAverageLengthHistogram = new CheckBox();

    private final BeanFieldGroup<QuantitativeAnalysisDTO> binder = new BeanFieldGroup<>(QuantitativeAnalysisDTO.class);

    private MultiColumnPanel panel;

    @Override
    public void initializeTab() {
        setCaption(provider.getProperty("view.tab.quantitative.analysis.title"));
        binder.setItemDataSource(new QuantitativeAnalysisDTO());
        binder.bindMemberFields(this);

        panel = initMainPanel();
        addComponent(panel);
        initListeners();
    }

    private void initListeners() {
        sentenceAverageLengthHistogram.addValueChangeListener(event -> sentenceUnit.setVisible(!sentenceUnit.isVisible()));
        wordAveragesLengthHistogram.addValueChangeListener(event -> wordUnit.setVisible(!wordUnit.isVisible()));
    }

    public MultiColumnPanel initMainPanel() {

        initializeWordUnit();
        initializeSentenceUnit();

        return new MultiColumnPanel
                .PanelBuilder(provider)
                .addPanel(null, new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addLeadTitle(provider.getProperty("label.word"))
                        .addTitle(provider.getProperty("label.filter"))
                        .addComponent(provider.getProperty("label.all.parts.of.speech"), allPartsOfSpeech)
                        .addRow(provider.getProperty("label.verb"), verb,
                                provider.getProperty("label.noun"), noun)
                        .addRow(provider.getProperty("label.adjective"), adjective,
                                provider.getProperty("label.adverb"), adverb)
                        .addComponent(provider.getProperty("label.namingUnit"), namingUnit)
                        .addComponentInForm(provider.getProperty("label.regular.expression"), wordRegularExpression)
                        .addTitle(provider.getProperty("label.tool"))
                        .addComponent(provider.getProperty("label.word.zipfa.histogram"), wordZipfHistogram)
                        .addComponent(provider.getProperty("label.word.average.length.histogram"), wordAveragesLengthHistogram)
                        .addComponentInForm(provider.getProperty("label.unit"), wordUnit)
                        .build())
                .addPanel(null, new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addLeadTitle(provider.getProperty(provider.getProperty("label.sentence")))
                        .addTitle(provider.getProperty("label.filter"))
                        .addComponentInForm(provider.getProperty("label.regular.expression"), sentenceRegularExpression)
                        .addTitle(provider.getProperty("label.tool"))
                        .addComponent(provider.getProperty("label.sentence.average.length.histogram"), sentenceAverageLengthHistogram)
                        .addComponentInForm(provider.getProperty("label.unit"), sentenceUnit)
                        .build())
                .addButton(getClearButton())
                .addButton(getAcceptButton())
                .build();
    }

    private void initializeSentenceUnit() {

        sentenceUnit.setVisible(false);
        sentenceUnit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        sentenceUnit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        sentenceUnit.addItem(pl.edu.pwr.chrono.infrastructure.Unit.WORD);
        sentenceUnit.setItemCaption(pl.edu.pwr.chrono.infrastructure.Unit.WORD,
                provider.getProperty("label.word"));
        sentenceUnit.select(pl.edu.pwr.chrono.infrastructure.Unit.WORD);
        sentenceUnit.addItem(pl.edu.pwr.chrono.infrastructure.Unit.LETTER);
        sentenceUnit.setItemCaption(pl.edu.pwr.chrono.infrastructure.Unit.LETTER,
                provider.getProperty("label.letter"));
    }

    private void initializeWordUnit() {
        wordUnit.setVisible(false);
        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        wordUnit.addItem(pl.edu.pwr.chrono.infrastructure.Unit.LETTER);
        wordUnit.setItemCaption(pl.edu.pwr.chrono.infrastructure.Unit.LETTER,
                provider.getProperty("label.letter"));
        wordUnit.select(pl.edu.pwr.chrono.infrastructure.Unit.LETTER);
        wordUnit.addItem(pl.edu.pwr.chrono.infrastructure.Unit.SYLLABLE);
        wordUnit.setItemCaption(pl.edu.pwr.chrono.infrastructure.Unit.SYLLABLE,
                provider.getProperty("label.syllable"));
    }


    public void showLoading(Boolean show) {
        if (show) {
            panel.showLoadingIndicator();
        } else {
            panel.hideLoadingIndicator();
        }
    }

    public void reset() {
        allPartsOfSpeech.setValue(false);
        adjective.setValue(false);
        verb.setValue(false);
        adverb.setValue(false);
        noun.setValue(false);
        namingUnit.setValue(false);
        wordRegularExpression.setValue("");
        sentenceRegularExpression.setValue("");
        wordAveragesLengthHistogram.setValue(false);
        wordZipfHistogram.setValue(false);
        sentenceAverageLengthHistogram.setValue(false);
    }

    public QuantitativeAnalysisDTO getQuantitativeAnalysisDTO() {
        try {
            binder.commit();
        } catch (FieldGroup.CommitException e) {
            QuantitativeAnalysisTab.log.info("Commit failed", e);
        }
        return binder.getItemDataSource().getBean();
    }
}
