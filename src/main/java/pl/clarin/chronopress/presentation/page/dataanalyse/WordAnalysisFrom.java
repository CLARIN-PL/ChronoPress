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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;
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

    @PropertyId("wordRegularExpression")
    private final TextField wordRegularExpression = new TextField();

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

        final Label txt1 = new Label();

        String t = "<p>System pozwala na obliczenia parametrów statystycznych dla wskazanych części mowy lub jednostek nazewniczych.</p>";

        txt1.setValue(t);
        txt1.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt1);

        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        final Label txt2 = new Label();

        String t1 = "<p>System rozpoznaje nazwy wielowyrazowe typu: \"Adam Mickiewicz\" lub \"Niemcy Zachodnie\" </p>";

        txt2.setValue(t1);
        txt2.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(txt2);

        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);

        final Label txt3 = new Label();

        String t2 = "<p>Histogram Zipfa przedstawia zależność częstości wystąpień wyrazów tekstowych (np. formy \"dom\" i \"domu\" traktowane są różne wyrazy) i liczby różnych wyrazów mających daną częstość</p></br>";

        txt3.setValue(t2);
        txt3.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent3 = new VerticalLayout();
        popupContent3.addComponent(txt3);

        PopupView help3 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent3);

        final Label txt4 = new Label();

        String t3 = "<p>System generuje wykres częstości wyrazów tekstowych (np. formy \"dom\" i \"domu\" traktowane są różne wyrazy) o różnych długościach</p></br>";

        txt4.setValue(t3);
        txt4.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent4 = new VerticalLayout();
        popupContent4.addComponent(txt4);

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

        FormLayout frm = new MFormLayout(wordRegularExpression)
                .withStyleName(ChronoTheme.COMPACT_FORM);

        wordUnitLayout.addComponents(new Label(provider.getProperty("label.unit")), wordUnit);
        wordUnitLayout.setSpacing(true);
        wordUnitLayout.setVisible(false);

        VerticalLayout layout = new MVerticalLayout()
                .with(new HorizontalLayout(allPartsOfSpeech, help), pos, new HorizontalLayout(namingUnit, help2), frm,
                        new HorizontalLayout(wordZipfHistogram, help3),
                        new HorizontalLayout(wordAveragesLengthHistogram, help4), wordUnitLayout);

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
        wordRegularExpression.setValue("");
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
        wordRegularExpression.setCaption(provider.getProperty("label.regular.expression"));
        wordRegularExpression.addValidator(regExpValidator);
        wordRegularExpression.addStyleName(ValoTheme.TEXTFIELD_TINY);

        wordAveragesLengthHistogram.addValueChangeListener(event -> wordUnitLayout.setVisible(!wordUnitLayout.isVisible()));

        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        wordUnit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        wordUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER);
        wordUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER, provider.getProperty("label.letter"));
        wordUnit.select(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER);
    }

    public WordAnalysisDTO getWordAnalysisDTO() throws FieldGroup.CommitException {
        binder.commit();
        return binder.getItemDataSource().getBean();
    }
}
