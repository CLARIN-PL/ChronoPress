package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.readmodel.dto.DataExplorationDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.MultiColumnPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.Tab;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@ViewScope
@Slf4j
public class DataExplorationTab extends Tab {

    @PropertyId("dataExplorationType")
    private final OptionGroup operationType = new OptionGroup();

    @PropertyId("lemma")
    private final TextField lemma = new TextField();

    @PropertyId("leftContextGap")
    private final ComboBox leftContextGap =  new ComboBox();

    @PropertyId("rightContextGap")
    private final ComboBox rightContextGap =  new ComboBox();

    @PropertyId("contextPos")
    private final ComboBox contextPos = new ComboBox();

    private final BeanFieldGroup<DataExplorationDTO> binder = new BeanFieldGroup<>(DataExplorationDTO.class);

    private MultiColumnPanel panel;

    private HorizontalLayout profileSubPanel;

    @Override
    public void initializeTab() {
        setCaption(provider.getProperty("view.tab.data.exploration.title"));
        binder.setItemDataSource(new DataExplorationDTO());
        binder.bindMemberFields(this);

        profileSubPanel = initProfile();
        profileSubPanel.setVisible(false);

        panel = initMainPanel();
        addComponent(panel);

        lemma.setVisible(false);
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
                        .addComponent(null, profileSubPanel)
                        .build())
                .addButton(getClearButton())
                .addButton(getAcceptButton())
                .build();
    }

    private HorizontalLayout initProfile(){
        HorizontalLayout h = new HorizontalLayout();

        contextPos.setCaption(provider.getProperty("view.tab.data.exploration.profile.pos"));
        contextPos.setNullSelectionAllowed(false);
        contextPos.addStyleName(ValoTheme.COMBOBOX_SMALL);
        contextPos.addItem(PartOfSpeech.adj);
        contextPos.setItemCaption(PartOfSpeech.adj, provider.getProperty("label.adjective"));

        contextPos.addItem(PartOfSpeech.noun);
        contextPos.setItemCaption(PartOfSpeech.noun, provider.getProperty("label.noun"));

        contextPos.addItem(PartOfSpeech.verb);
        contextPos.setItemCaption(PartOfSpeech.verb, provider.getProperty("label.noun"));

        contextPos.addItem(PartOfSpeech.adverb);
        contextPos.setItemCaption(PartOfSpeech.adverb, provider.getProperty("label.adverb"));

        contextPos.setValue(PartOfSpeech.adj);

        List<Integer> numbers = Arrays.asList(1,2,3,4,5);

        leftContextGap.addStyleName(ValoTheme.COMBOBOX_SMALL);
        leftContextGap.setCaption(provider.getProperty("view.tab.data.exploration.profile.left.context.gap"));
        leftContextGap.addItems(numbers);

        rightContextGap.addStyleName(ValoTheme.COMBOBOX_SMALL);
        rightContextGap.setCaption(provider.getProperty("view.tab.data.exploration.profile.right.context.gap"));
        rightContextGap.addItems(numbers);

        h.addComponent(contextPos);
        h.addComponent(leftContextGap);
        h.addComponent(rightContextGap);
        return  h;
    }

    private void initializeOperations() {
        operationType.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        operationType.addItem(DataExplorationType.LEXEME_FREQUENCY_LIST);
        operationType.setItemCaption(DataExplorationType.LEXEME_FREQUENCY_LIST,
                provider.getProperty("label.lexeme.frequency.list"));
        operationType.select(DataExplorationType.LEXEME_FREQUENCY_LIST);

        operationType.addItem(DataExplorationType.NOT_LEMMATIZED_FREQUENCY_LIST);
        operationType.setItemCaption(DataExplorationType.NOT_LEMMATIZED_FREQUENCY_LIST,
                provider.getProperty("label.not.lemmatized.frequency.list"));

        operationType.setItemCaption(DataExplorationType.PLACE_NAME_MAP,
                provider.getProperty("label.place.map"));
        operationType.addItem(DataExplorationType.PLACE_NAME_MAP);

        operationType.addItem(DataExplorationType.LEXEME_CONCORDANCE);
        operationType.setItemCaption(DataExplorationType.LEXEME_CONCORDANCE,
                provider.getProperty("label.concordance.list"));

        operationType.addValueChangeListener(event -> {
            if (event.getProperty().getValue() == DataExplorationType.LEXEME_CONCORDANCE) {
                lemma.setVisible(true);
                profileSubPanel.setVisible(false);
            } else if (event.getProperty().getValue() == DataExplorationType.PROFILE) {
                lemma.setVisible(true);
                profileSubPanel.setVisible(true);
            } else {
                lemma.setVisible(false);
                profileSubPanel.setVisible(false);
            }

        });

        operationType.addItem(DataExplorationType.PROFILE);
        operationType.setItemCaption(DataExplorationType.PROFILE,
                provider.getProperty("label.profile.list"));

    }

    public void reset() {
        lemma.clear();
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
        LEXEME_FREQUENCY_LIST, NOT_LEMMATIZED_FREQUENCY_LIST, LEXEME_CONCORDANCE, PLACE_NAME_MAP, PROFILE
    }

    public enum PartOfSpeech{
        verb, noun, adj, adverb
    }
}
