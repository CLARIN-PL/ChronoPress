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
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.tokenfield.TokenField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.lexicalfield.boundary.LexicalFieldFacade;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.shered.dto.Time;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;
import pl.clarin.chronopress.presentation.shered.layout.MComboBox;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class TimeSeriesForm extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    LexicalFieldFacade lexicalFieldFacade;

    @PropertyId("lexeme")
    private final TokenField lexeme = new TokenField(new VerticalLayout());

    private final TokenField userLexicalField = new TokenField(new VerticalLayout());

    private final MComboBox<LexicalField> lexical = new MComboBox("groupName", LexicalField.class);

    @PropertyId("movingAverageWindowSize")
    private final TextField movingAverageWindowSize = new TextField();

    @PropertyId("unit")
    private final OptionGroup unit = new OptionGroup();

    @PropertyId("movingAverage")
    private final CheckBox movingAverage = new CheckBox();

    private final BeanFieldGroup<TimeSeriesDTO> binder = new BeanFieldGroup<>(TimeSeriesDTO.class);

    @PostConstruct
    private void init() {
        binder.setItemDataSource(new TimeSeriesDTO());
        binder.bindMemberFields(this);

        Label info1 = new Label(VaadinUI.infoMessage(provider.getProperty("view.time.series.info1")));
        info1.setContentMode(ContentMode.HTML);

        Label info2 = new Label(VaadinUI.infoMessage(provider.getProperty("view.time.series.info2")));
        info2.setContentMode(ContentMode.HTML);

        Label info3 = new Label(VaadinUI.infoMessage(provider.getProperty("view.time.series.info3")));
        info3.setContentMode(ContentMode.HTML);

        VerticalLayout popupContent1 = new VerticalLayout();
        popupContent1.addComponent(info1);

        VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(info2);

        VerticalLayout popupContent3 = new VerticalLayout();
        popupContent3.addComponent(info3);

        // The component itself
        PopupView help1 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent1);
        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);
        PopupView help3 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent3);

        unit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        unit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);

        unit.addItem(Time.MONTH);
        unit.setItemCaption(Time.MONTH, provider.getProperty("label.month"));
        unit.select(Time.MONTH);

        unit.addItem(Time.YEAR);
        unit.setItemCaption(Time.YEAR, provider.getProperty("label.year"));

        HorizontalLayout lexFiledTxt = new MHorizontalLayout(help2, lexical);
        lexFiledTxt.setCaption(provider.getProperty("label.lexical"));
        lexical.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexical.addBeans(lexicalFieldFacade.findAll());

        HorizontalLayout lexTxt = new MHorizontalLayout(help1, lexeme);
        lexTxt.setCaption(provider.getProperty("label.word"));

        lexeme.addStyleName(TokenField.STYLE_TOKENFIELD);
        lexeme.addStyleName(ChronoTheme.TOKENFIELD);
        lexeme.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexeme.setTokenInsertPosition(TokenField.InsertPosition.AFTER);

        HorizontalLayout usrlexTxt = new MHorizontalLayout(help3, userLexicalField);
        usrlexTxt.setCaption(provider.getProperty("label.custom.lexical.field"));

        userLexicalField.addStyleName(TokenField.STYLE_TOKENFIELD);
        userLexicalField.addStyleName(ChronoTheme.TOKENFIELD);
        userLexicalField.addStyleName(ValoTheme.COMBOBOX_TINY);
        userLexicalField.setTokenInsertPosition(TokenField.InsertPosition.AFTER);

        movingAverage.setCaption(provider.getProperty("label.tool.moving.average"));
        movingAverageWindowSize.setCaption(provider.getProperty("label.add.movin.avarage"));
        movingAverageWindowSize.setVisible(false);
        movingAverage.addValueChangeListener(event
                -> movingAverageWindowSize.setVisible(!movingAverageWindowSize.isVisible()));

        FormLayout form = new MFormLayout(
                lexTxt, lexFiledTxt, usrlexTxt, unit, movingAverage,
                movingAverageWindowSize);

        setSizeUndefined();
        setCompositionRoot(form);
    }

    public void reset() {
        lexeme.clear();
        lexical.setValue(null);
    }

    public TimeSeriesDTO getTimeSeriesDTO() throws FieldGroup.CommitException {
        binder.commit();
        if (lexical.getValue() != null) {
            LexicalField item = lexical.getBean();
            Set<String> names = item.getLexicalnames();
            if (names.size() > 0) {
                binder.getItemDataSource().getBean().getLexeme().addAll(names);
                binder.getItemDataSource().getBean().setAsSumOfResults(true);
            }
        }
        if (lexical.getValue() != null) {
            binder.getItemDataSource().getBean().setAsSumOfResults(true);
        } else {
            binder.getItemDataSource().getBean().setAsSumOfResults(false);
        }

        return binder.getItemDataSource().getBean();
    }
}
