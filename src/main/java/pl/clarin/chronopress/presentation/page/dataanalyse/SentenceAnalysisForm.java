package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;
import pl.clarin.chronopress.presentation.shered.validators.RegularExpressionValidator;

public class SentenceAnalysisForm extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    RegularExpressionValidator regExpValidator;

    @PropertyId("sentenceUnit")
    private final OptionGroup sentenceUnit = new OptionGroup();

    @PropertyId("sentenceAverageLengthHistogram")
    private final CheckBox sentenceAverageLengthHistogram = new CheckBox();

    private final BeanFieldGroup<SentenceAnalysisDTO> binder = new BeanFieldGroup<>(SentenceAnalysisDTO.class);

    @PostConstruct
    public void init() {
        binder.setItemDataSource(new SentenceAnalysisDTO());
        binder.bindMemberFields(this);

        HorizontalLayout sentenceUnitLayout = new HorizontalLayout();
        sentenceUnitLayout.setSpacing(true);
        sentenceUnitLayout.setVisible(true);
        sentenceUnitLayout.addComponents(new Label(provider.getProperty("label.unit")), sentenceUnit);

        sentenceAverageLengthHistogram.setCaption(provider.getProperty("label.sentence.average.length.histogram"));

        sentenceUnit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        sentenceUnit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        sentenceUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.WORD);
        //provider.getProperty("label.word")
        sentenceUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.WORD, "wyraz");
        sentenceUnit.select(pl.clarin.chronopress.presentation.shered.dto.Unit.WORD);
        sentenceUnit.addItem(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER);
        //provider.getProperty("label.letter")
        sentenceUnit.setItemCaption(pl.clarin.chronopress.presentation.shered.dto.Unit.LETTER, "litera");

        VerticalLayout layout = new MVerticalLayout()
                .withStyleName(ChronoTheme.COMPACT_FORM)
                .with(sentenceAverageLengthHistogram, sentenceUnitLayout);

        setWidthUndefined();
        setCompositionRoot(layout);
    }

    public void reset() {
        sentenceAverageLengthHistogram.setValue(false);
    }

    public SentenceAnalysisDTO getSentenceAnalysisDTO() throws FieldGroup.CommitException {
        binder.commit();
        return binder.getItemDataSource().getBean();
    }
}
