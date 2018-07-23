/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.Unit;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.validators.RegularExpressionValidator;

public class WordAnalysisFrom extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    RegularExpressionValidator regExpValidator;

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

    //@PropertyId("wordRegularExpression")
    //private final TextField wordRegularExpression = new TextField();
    @PropertyId("wordUnit")
    private final OptionGroup wordUnit = new OptionGroup();

    @PropertyId("wordAveragesLengthHistogram")
    private final CheckBox wordAveragesLengthHistogram = new CheckBox();

    @PropertyId("wordZipfHistogram")
    private final CheckBox wordZipfHistogram = new CheckBox();

    private final BeanFieldGroup<WordAnalysisDTO> binder = new BeanFieldGroup<>(WordAnalysisDTO.class);

    private final HorizontalLayout wordUnitLayout = new HorizontalLayout();

    @PostConstruct
    public void init() {
        binder.setItemDataSource(new WordAnalysisDTO());
        binder.bindMemberFields(this);
        initializeComponents();

        final Label info1 = new Label();
        info1.setValue(provider.getProperty("label.word.anal.form.info1"));
        info1.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(info1);

        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        final Label info2 = new Label();
        info2.setValue(provider.getProperty("label.word.anal.form.info2"));
        info2.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(info2);

        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);

        final Label info3 = new Label();
        info3.setValue(provider.getProperty("label.word.anal.form.info3"));
        info3.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent3 = new VerticalLayout();
        popupContent3.addComponent(info3);

        PopupView help3 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent3);

        final Label info4 = new Label();
        info4.setValue(provider.getProperty("label.word.anal.form.info4"));
        info4.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent4 = new VerticalLayout();
        popupContent4.addComponent(info4);

        PopupView help4 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent4);

        VerticalLayout left = new MVerticalLayout()
                .withSpacing(false)
                .withMargin(false)
                .with(verb, noun);

        VerticalLayout right = new MVerticalLayout()
                .withSpacing(false)
                .withMargin(false)
                .with(adjective, adverb);

        HorizontalLayout pos = new MHorizontalLayout()
                .withSpacing(true)
                .withMargin(false)
                .with(left, right);

        // FormLayout frm = new MFormLayout(wordRegularExpression)
        //         .withStyleName(ChronoTheme.COMPACT_FORM);
        wordUnitLayout.addComponents(new Label(provider.getProperty("label.unit")), wordUnit);
        wordUnitLayout.setSpacing(true);
        wordUnitLayout.setVisible(true);

        VerticalLayout layout = new MVerticalLayout()
                .with(new MHorizontalLayout(allPartsOfSpeech, help).withSpacing(true),
                        pos,
                        new MHorizontalLayout(namingUnit, help2).withSpacing(true),
                        new MHorizontalLayout(wordZipfHistogram, help3).withSpacing(true),
                        new MHorizontalLayout(wordAveragesLengthHistogram, help4).withSpacing(true),
                        wordUnitLayout);

        setWidthUndefined();
        setCompositionRoot(layout);
    }

    public void reset() {
        allPartsOfSpeech.setValue(false);
        adjective.setValue(false);
        verb.setValue(false);
        adverb.setValue(false);
        noun.setValue(false);
        namingUnit.setValue(false);
        wordAveragesLengthHistogram.setValue(false);
        wordZipfHistogram.setValue(false);
    }

    private void initializeComponents() {

        allPartsOfSpeech.setCaption(provider.getProperty("label.all.parts.of.speech"));
        verb.setCaption(provider.getProperty("label.verb"));
        noun.setCaption(provider.getProperty("label.noun"));
        adjective.setCaption(provider.getProperty("label.adjective"));
        adverb.setCaption(provider.getProperty("label.adverb"));
        namingUnit.setCaption(provider.getProperty("label.namingUnit"));
        wordZipfHistogram.setCaption(provider.getProperty("label.word.zipfa.histogram"));
        wordAveragesLengthHistogram.setCaption(provider.getProperty("label.word.average.length.histogram"));

        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);

        wordUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER);
        wordUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER, provider.getProperty("label.letter"));

        wordUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.SYLLABLE);
        wordUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.SYLLABLE, provider.getProperty("label.syllable"));

        wordUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.FONEM);
        wordUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.FONEM, provider.getProperty("label.fonem"));

        wordUnit.select(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER);
    }

    public WordAnalysisDTO getWordAnalysisDTO() throws FieldGroup.CommitException {
        binder.commit();
        return binder.getItemDataSource().getBean();
    }
}
