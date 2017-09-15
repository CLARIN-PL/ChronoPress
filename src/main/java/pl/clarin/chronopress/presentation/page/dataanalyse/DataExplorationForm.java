package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.DataExplorationDTO;

public class DataExplorationForm extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @PropertyId("dataExplorationType")
    private final OptionGroup operationType = new OptionGroup();

    @PropertyId("lemma")
    private final TextField lemma = new TextField();

    @PropertyId("leftContextGap")
    private final ComboBox leftContextGap = new ComboBox();

    @PropertyId("rightContextGap")
    private final ComboBox rightContextGap = new ComboBox();

    @PropertyId("contextPos")
    private final ComboBox contextPos = new ComboBox();

    @PropertyId("caseSensitive")
    private final CheckBox caseSensitive = new CheckBox("Uwzględnij wielkość liter");

    private final BeanFieldGroup<DataExplorationDTO> binder = new BeanFieldGroup<>(DataExplorationDTO.class);

    private HorizontalLayout profileSubPanel;
    private FormLayout lemmaPanel;

    Label txt = new Label();

    @PostConstruct
    public void init() {

        binder.setItemDataSource(new DataExplorationDTO());
        binder.bindMemberFields(this);

        profileSubPanel = initProfile();
        profileSubPanel.setVisible(false);

        lemmaPanel = initLemma();
        lemmaPanel.setVisible(false);

        operationType.setVisible(false);
        initializeOperations();

        VerticalLayout layout = new MVerticalLayout()
                .withMargin(false)
                .with(operationType, lemmaPanel, profileSubPanel);

        setSizeUndefined();
        setCompositionRoot(layout);
    }

    public void setLemmaHelp(String tekst) {
        txt.setValue(tekst);
    }

    private FormLayout initLemma() {

        lemma.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        lemma.setWidth("250px");
        caseSensitive.addStyleName(ValoTheme.CHECKBOX_SMALL);

        txt.setContentMode(ContentMode.HTML);
        VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt);
        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        HorizontalLayout wrapper = new MHorizontalLayout()
                .withCaption("Wpisz wyraz")
                .with(lemma, help);

        return new MFormLayout(wrapper, caseSensitive)
                .withMargin(false);

    }

    private HorizontalLayout initProfile() {

        contextPos.setCaption("Wyrazy kotekstowe");
        contextPos.setNullSelectionAllowed(false);
        contextPos.addStyleName(ValoTheme.COMBOBOX_SMALL);

        contextPos.addItem(PartOfSpeech.all);
        contextPos.setItemCaption(PartOfSpeech.all, "wszystkie");

        contextPos.addItem(PartOfSpeech.adj);
        contextPos.setItemCaption(PartOfSpeech.adj, provider.getProperty("label.adjective"));

        contextPos.addItem(PartOfSpeech.noun);
        contextPos.setItemCaption(PartOfSpeech.noun, provider.getProperty("label.noun"));

        contextPos.addItem(PartOfSpeech.verb);
        contextPos.setItemCaption(PartOfSpeech.verb, provider.getProperty("label.verb"));

        contextPos.addItem(PartOfSpeech.adverb);
        contextPos.setItemCaption(PartOfSpeech.adverb, provider.getProperty("label.adverb"));

        contextPos.setValue(PartOfSpeech.adj);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        leftContextGap.addStyleName(ValoTheme.COMBOBOX_SMALL);
        leftContextGap.setCaption("Lewy kontekst");
        leftContextGap.addItems(numbers);

        rightContextGap.addStyleName(ValoTheme.COMBOBOX_SMALL);
        rightContextGap.setCaption("Prawy kontekst");
        rightContextGap.addItems(numbers);

        final Label txt1 = new Label();

        txt1.setValue("Wyrazy kontekstowe (kolokaty) można ograniczyć do wskazanych części mowy.");
        txt1.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt1);

        final PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        final Label txt2 = new Label();

        txt2.setValue("Szerokość kontekstu określona jest liczbą wyrazów tekstowych.");
        txt2.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(txt2);

        final PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);

        return new MHorizontalLayout()
                .with(new HorizontalLayout(contextPos, help), new HorizontalLayout(leftContextGap, rightContextGap, help2));
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
                lemmaPanel.setVisible(true);
                profileSubPanel.setVisible(false);
            } else if (event.getProperty().getValue() == DataExplorationType.PROFILE) {
                lemmaPanel.setVisible(true);
                profileSubPanel.setVisible(true);
            } else {
                lemmaPanel.setVisible(false);
                profileSubPanel.setVisible(false);
            }

        });

        operationType.addItem(DataExplorationType.PROFILE);
        operationType.setItemCaption(DataExplorationType.PROFILE,
                provider.getProperty("label.profile.list"));

    }

    public void selectOptionType(DataExplorationType type) {
        operationType.select(type);
    }

    public void setLemma(String txt) {
        lemma.setValue(txt);
    }

    public void selectConcordance() {
        operationType.select(DataExplorationType.LEXEME_CONCORDANCE);
    }

    public void reset() {
        lemma.clear();
        caseSensitive.setValue(Boolean.FALSE);
    }

    public DataExplorationDTO getDataExplorationDTO() throws FieldGroup.CommitException {
        binder.commit();
        return binder.getItemDataSource().getBean();
    }

    public enum DataExplorationType {
        LEXEME_FREQUENCY_LIST, NOT_LEMMATIZED_FREQUENCY_LIST, LEXEME_CONCORDANCE, PLACE_NAME_MAP, PROFILE
    }

    public enum PartOfSpeech {
        all, verb, noun, adj, adverb
    }
}
