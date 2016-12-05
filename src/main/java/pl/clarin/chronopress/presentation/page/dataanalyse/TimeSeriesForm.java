/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.tokenfield.TokenField;
import org.vaadin.viritin.layouts.MFormLayout;
import pl.clarin.chronopress.business.lexicalfield.boundary.LexicalFieldFacade;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
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

        unit.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        unit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);

        unit.addItem(Time.MONTH);
        unit.setItemCaption(Time.MONTH, provider.getProperty("label.month"));
        unit.select(Time.MONTH);

        unit.addItem(Time.YEAR);
        unit.setItemCaption(Time.YEAR, provider.getProperty("label.year"));

        lexical.setCaption(provider.getProperty("label.lexical"));
        lexical.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexical.addBeans(lexicalFieldFacade.findAll());

        lexeme.setCaption(provider.getProperty("label.lexeme"));
        lexeme.addStyleName(TokenField.STYLE_TOKENFIELD);
        lexeme.addStyleName(ChronoTheme.TOKENFIELD);
        lexeme.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexeme.setTokenInsertPosition(TokenField.InsertPosition.AFTER);

        movingAverage.setCaption(provider.getProperty("label.tool.moving.average"));
        movingAverageWindowSize.setCaption(provider.getProperty("label.tool.moving.average.window.size"));
        movingAverageWindowSize.setVisible(false);
        movingAverage.addValueChangeListener(event
                -> movingAverageWindowSize.setVisible(!movingAverageWindowSize.isVisible()));

        FormLayout form = new MFormLayout(
                lexeme, lexical, unit, movingAverage,
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
