package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.readmodel.dto.DataExplorationDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.MultiColumnPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.Tab;

@SpringComponent
@ViewScope
@Slf4j
public class DataExplorationTab extends Tab {

    @PropertyId("dataExplorationType")
    private final OptionGroup operationType = new OptionGroup();

    @PropertyId("lemma")
    private final TextField lemma = new TextField();

    private final BeanFieldGroup<DataExplorationDTO> binder = new BeanFieldGroup<>(DataExplorationDTO.class);

    private MultiColumnPanel panel;

    @Override
    public void initializeTab() {
        setCaption(provider.getProperty("view.tab.data.exploration.title"));
        binder.setItemDataSource(new DataExplorationDTO());
        binder.bindMemberFields(this);

        panel = initMainPanel();
        addComponent(panel);

    }

    public MultiColumnPanel initMainPanel() {
        initializeOperations();
        return new MultiColumnPanel
                .PanelBuilder(provider)
                .addPanel(null, new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addTitle(provider.getProperty("label.operations"))
                        .addComponent(null, operationType)
                        .addComponentInForm(provider.getProperty("label.lemma"), lemma)
                        .build())
                .addButton(getClearButton())
                .addButton(getAcceptButton())
                .build();
    }

    private void initializeOperations() {
        operationType.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        operationType.addItem(DataExplorationType.LEXEME_FREQUENCY_LIST);
        operationType.setItemCaption(DataExplorationType.LEXEME_FREQUENCY_LIST, provider.getProperty("label.lexeme.frequency.list"));
        operationType.select(DataExplorationType.LEXEME_FREQUENCY_LIST);
        operationType.addItem(DataExplorationType.LEXEME_CONCORDANCE);
        operationType.setItemCaption(DataExplorationType.LEXEME_CONCORDANCE, provider.getProperty("label.lexeme.concordance"));
    }

    public void showLoading(Boolean show) {
        if (show) {
            panel.showLoadingIndicator();
        } else {
            panel.hideLoadingIndicator();
        }
    }

    public DataExplorationDTO getDataExplorationDTO() throws FieldGroup.CommitException {
        binder.commit();
        return binder.getItemDataSource().getBean();
    }

    public enum DataExplorationType {
        LEXEME_FREQUENCY_LIST, LEXEME_CONCORDANCE
    }
}
