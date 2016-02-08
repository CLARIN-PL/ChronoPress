package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.hene.popupbutton.PopupButton;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.SearchableTablePanel;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 05.02.16.
 */
@SpringComponent
@ViewScope
public class DataSelectionTab extends VerticalLayout{

    private final Button acceptButton = new Button("Akceptuj");
    private final Button clearButton  = new Button("Wyczyść");

    private final ComboBoxMultiselect years = new ComboBoxMultiselect();
    private final ComboBoxMultiselect titles = new ComboBoxMultiselect();
    private final ComboBoxMultiselect periods = new ComboBoxMultiselect();
    private final ComboBoxMultiselect expositions = new ComboBoxMultiselect();
    private final ComboBoxMultiselect audience = new ComboBoxMultiselect();
    private final PopupButton authors = new PopupButton();

    private final VerticalLayout mainPanelContent = new VerticalLayout();
    private final Panel results = new Panel();
    private final VerticalLayout loadingIndicator = initializeLoading();
    private final TextField sampleCount = new TextField();
    private final TextField wordCount = new TextField();

    @Autowired
    private SearchableTablePanel searchAuthorsPanel;

    @PostConstruct
    public void init(){
        setMargin(true);
        setCaption("Selekcja danych");
        setSpacing(true);

        addComponent(initMainPanel());
        initializeResultPanel();
    }

    private void initializeResultPanel() {
        results.setCaption("Rezultat seleckcji");
        results.addStyleName(ChronoTheme.RESULT_PANEL);
        results.addStyleName(ValoTheme.PANEL_BORDERLESS);
        results.setSizeUndefined();
        results.setContent(initializeResultContentPanel());
        results.setVisible(false);
    }

    public HorizontalLayout initMainPanel(){

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);

        mainPanelContent.setSizeUndefined();
        mainPanelContent.addComponent(initializeComboBoxes());

        HorizontalLayout buttonBar = buildButtonBar();
        mainPanelContent.addComponent(buttonBar);
        mainPanelContent.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);

        wrapper.addComponent(mainPanelContent);
        wrapper.setComponentAlignment(mainPanelContent, Alignment.MIDDLE_CENTER);

        return  wrapper;
    }

    public VerticalLayout initializeLoading(){
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setIndeterminate(true);

            HorizontalLayout loadingNotification = new HorizontalLayout();
            loadingNotification.setSpacing(true);
            loadingNotification.addComponents(progressBar, new Label("Wczytywanie ..."));

            layout.addComponents(loadingNotification);
            layout.setComponentAlignment(loadingNotification, Alignment.MIDDLE_CENTER);

            return layout;
    }

    public void showLoadingIndicator(){
        mainPanelContent.removeComponent(results);
        mainPanelContent.addComponent(loadingIndicator);
    }

    public void showResults(Integer sc, Integer wc){
        mainPanelContent.removeComponent(loadingIndicator);
        wordCount.setValue(Integer.toString(wc));
        sampleCount.setValue(Integer.toString(sc));
        results.setVisible(true);
        mainPanelContent.addComponent(results);
        mainPanelContent.setComponentAlignment(results, Alignment.MIDDLE_LEFT);
    }

    public VerticalLayout initializeResultContentPanel(){

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(new MarginInfo(false,true,false,true));
        sampleCount.setCaption("Liczba próbek");
        wordCount.setCaption("Liczba wyrazów");

        FormLayout layout = new FormLayout();
        layout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(sampleCount);
        layout.addComponent(wordCount);
        wrapper.addComponent(layout);
        return wrapper;
    }

    public HorizontalLayout initializeComboBoxes(){

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        FormLayout left = new FormLayout();
        left.setSpacing(true);

        years.setCaption("Lata");
        years.addStyleName(ValoTheme.COMBOBOX_TINY);
        years.setInputPrompt("Wszystkie");
        left.addComponent(years);

        titles.setCaption("Tytuły");
        titles.addStyleName(ValoTheme.COMBOBOX_TINY);
        titles.setInputPrompt("Wszystkie");
        left.addComponent(titles);

        HorizontalLayout authorWrapper = new HorizontalLayout();
        authorWrapper.setCaption("Autor");
        authors.addStyleName(ValoTheme.BUTTON_TINY);
        authors.addStyleName(ChronoTheme.POPUP_BUTTON);
        authors.setContent(searchAuthorsPanel);
        authors.setWidth(160, Unit.PIXELS);
        authors.setCaption("Wszystkie");
        authorWrapper.addComponent(authors);
        left.addComponent(authorWrapper);


        FormLayout right = new FormLayout();
        right.setSpacing(true);

        audience.setCaption("Grupa odbiorcza");
        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        audience.setInputPrompt("Wszystkie");
        right.addComponent(audience);

        periods.setCaption("Typ periodyku");
        periods.addStyleName(ValoTheme.COMBOBOX_TINY);
        periods.setInputPrompt("Wszystkie");
        right.addComponent(periods);

        expositions.setCaption("Ekspozycja");
        expositions.addStyleName(ValoTheme.COMBOBOX_TINY);
        expositions.setInputPrompt("Wszystkie");
        right.addComponent(expositions);


        layout.addComponent(left);
        layout.addComponent(right);
        return  layout;
    }

    public HorizontalLayout buildButtonBar(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeUndefined();
        layout.setSpacing(true);

        acceptButton.setIcon(FontAwesome.CHECK);
        acceptButton.addStyleName(ValoTheme.BUTTON_SMALL);
        acceptButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        clearButton.addStyleName(ValoTheme.BUTTON_SMALL);
        clearButton.addClickListener(event -> {
            years.unselectAll();
            titles.unselectAll();
            expositions.unselectAll();
            periods.unselectAll();
        });

        layout.addComponent(clearButton);
        layout.addComponent(acceptButton);

        return layout;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public ComboBoxMultiselect getYears() {
        return years;
    }

    public ComboBoxMultiselect getTitles() {
        return titles;
    }

    public ComboBoxMultiselect getPeriods() {
        return periods;
    }

    public ComboBoxMultiselect getExpositions() {
        return expositions;
    }

    public SearchableTablePanel getSearchAuthorsPanel() {
        return searchAuthorsPanel;
    }
}
