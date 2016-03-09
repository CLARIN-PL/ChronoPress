package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.tokenfield.TokenField;
import pl.edu.pwr.chrono.domain.LexicalField;
import pl.edu.pwr.chrono.infrastructure.Time;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesDTO;
import pl.edu.pwr.chrono.repository.LexicalFieldRepository;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.EntityComboBox;
import pl.edu.pwr.chrono.webui.infrastructure.components.MultiColumnPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.Tab;
import pl.edu.pwr.chrono.webui.infrastructure.validators.RegularExpressionValidator;

import java.util.Set;

@Slf4j
@SpringComponent
@ViewScope
public class TimeSeriesTab extends Tab {

    @PropertyId("lexeme")
    private final TokenField lexeme = new TokenField(new VerticalLayout());

    private final EntityComboBox<LexicalField> lexical = new EntityComboBox("groupName", LexicalField.class);

    @PropertyId("regularExpression")
    private final TextField regularExpression = new TextField();

    @PropertyId("movingAverageWindowSize")
    private final TextField movingAverageWindowSize = new TextField();

    @PropertyId("unit")
    private final OptionGroup unit = new OptionGroup();

    @PropertyId("timeSeriesCalculation")
    private final CheckBox timeSeries = new CheckBox();

    @PropertyId("movingAverage")
    private final CheckBox movingAverage = new CheckBox();

    private final BeanFieldGroup<TimeSeriesDTO> binder = new BeanFieldGroup<>(TimeSeriesDTO.class);

    private MultiColumnPanel panel;

    @Autowired
    private RegularExpressionValidator regularExpressionValidator;

    @Autowired
    private LexicalFieldRepository repository;

    @Override
    public void initializeTab() {
        setCaption(provider.getProperty("view.tab.time.series.title"));
        binder.bindMemberFields(this);
        binder.setItemDataSource(new TimeSeriesDTO());

        panel = initPanel();
        addComponent(panel);

        initializeTimeUnit();
        initLexemeField();

        lexical.load(repository.findAll());
        movingAverageWindowSize.setVisible(false);
        movingAverage.addValueChangeListener(event ->
                movingAverageWindowSize.setVisible(!movingAverageWindowSize.isVisible()));
    }

    private void initLexemeField() {

        lexeme.addStyleName(TokenField.STYLE_TOKENFIELD);
        lexeme.addStyleName(ChronoTheme.TOKENFIELD);
        lexeme.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexeme.setTokenInsertPosition(TokenField.InsertPosition.AFTER);

        regularExpression.addValidator(regularExpressionValidator);
    }

    private void initializeTimeUnit() {

        unit.addStyleName(ValoTheme.OPTIONGROUP_SMALL);

        unit.addItem(Time.MONTH);
        unit.setItemCaption(Time.MONTH, provider.getProperty("label.month"));
        unit.select(Time.MONTH);

        unit.addItem(Time.YEAR);
        unit.setItemCaption(Time.YEAR, provider.getProperty("label.year"));

    }

    public void reset() {
        lexeme.clear();
        regularExpression.setValue("");
        timeSeries.setValue(false);
        lexical.setValue(null);
    }

    public MultiColumnPanel initPanel() {
        return new MultiColumnPanel
                .PanelBuilder(provider)
                .addPanel(provider.getProperty("label.time.series.selection"), new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addComponent(provider.getProperty("label.lexeme"), lexeme)
                        .addComponent(provider.getProperty("label.lexical"), lexical)
                        .addComponent(provider.getProperty("label.regular.expression"), regularExpression)
                        .buildWithFromLayout())
                .addPanel(provider.getProperty("label.time.series.units"), new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addComponent(null, unit)
                        .build())
                .addPanel(provider.getProperty("label.time.series.tools"), new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addComponent(provider.getProperty("label.tool.time.series"), timeSeries)
                        .addComponent(provider.getProperty("label.tool.moving.average"), movingAverage)
                        .addComponent(provider.getProperty("label.tool.moving.average.window.size"), movingAverageWindowSize)
                        .build())
                .addButton(getClearButton())
                .addButton(getAcceptButton())
                .build();
    }

    public void showLoading(Boolean show) {
        if (show) {
            panel.showLoadingIndicator();
        } else {
            panel.hideLoadingIndicator();
        }
    }

    public TimeSeriesDTO getTimeSeriesDTO() throws FieldGroup.CommitException {
        binder.commit();
        if (lexical.getValue() != null) {
            LexicalField item = lexical.getContainer().getItem(lexical.getValue()).getBean();
            Set<String> names = item.getLexicalnames();
            if (names.size() > 0) {
                binder.getItemDataSource().getBean().getLexeme().addAll(names);
            }
        }

        return binder.getItemDataSource().getBean();
    }
}
