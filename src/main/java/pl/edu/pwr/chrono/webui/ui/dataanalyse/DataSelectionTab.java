package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.hene.popupbutton.PopupButton;

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

    private final  Panel results = new Panel();
    private final  TextField sampleCount = new TextField();
    private final  TextField wordCount = new TextField();

    @PostConstruct
    public void init(){
        setMargin(true);
        setCaption("Selekcja danych");
        setSpacing(true);

        HorizontalLayout  mainPanel = initMainPanel();

        results.setCaption("Rezultat wyszukania");
        results.setContent(initializeResultPanel());
        results.setSizeUndefined();
        results.setVisible(false);

        addComponent(mainPanel);
        addComponent(results);
        setComponentAlignment(results , Alignment.MIDDLE_CENTER);
    }

    public HorizontalLayout initMainPanel(){

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.addComponent(initializeComboBoxes());

        HorizontalLayout buttonBar = buildButtonBar();
        layout.addComponent(buttonBar);
        layout.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);

        wrapper.addComponent(layout);
        wrapper.setComponentAlignment(layout , Alignment.MIDDLE_CENTER);

        return  wrapper;
    }

    public void showResults(Integer sc, Integer wc){
        wordCount.setValue(Integer.toString(sc));
        sampleCount.setValue(Integer.toString(wc));
        results.setVisible(true);
    }

    public FormLayout initializeResultPanel(){

        sampleCount.setCaption("Liczba próbek");
        wordCount.setCaption("Liczba wyrazów");

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(sampleCount);
        layout.addComponent(wordCount);
        return layout;
    }

    public HorizontalLayout initializeComboBoxes(){

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        FormLayout left = new FormLayout();
        left.setSpacing(true);

        years.setCaption("Lata");
        years.addStyleName(ValoTheme.COMBOBOX_TINY);
        left.addComponent(years);

        titles.setCaption("Tytuły");
        titles.addStyleName(ValoTheme.COMBOBOX_TINY);
        left.addComponent(titles);

        audience.setCaption("Grupa odbiorcza");
        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        left.addComponent(audience);

        FormLayout right = new FormLayout();
        right.setSpacing(true);

        HorizontalLayout popupLayout = new HorizontalLayout();
        popupLayout.addComponent(new Grid());

        authors.setCaption("Autor");
        authors.addStyleName(ValoTheme.BUTTON_TINY);
        authors.setContent(popupLayout);
        right.addComponent(authors);

        periods.setCaption("Typ periodyku");
        periods.addStyleName(ValoTheme.COMBOBOX_TINY);
        right.addComponent(periods);

        expositions.setCaption("Ekspozycja");
        expositions.addStyleName(ValoTheme.COMBOBOX_TINY);
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
}
