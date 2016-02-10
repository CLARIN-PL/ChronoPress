package pl.edu.pwr.chrono.webui.ui.dataanalyse;

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
import pl.edu.pwr.chrono.webui.infrastructure.components.Tab;

/**
 * Created by tnaskret on 05.02.16.
 */
@SpringComponent
@ViewScope
public class DataSelectionTab extends Tab {

    private final ComboBoxMultiselect years = new ComboBoxMultiselect();
    private final ComboBoxMultiselect titles = new ComboBoxMultiselect();
    private final ComboBoxMultiselect periods = new ComboBoxMultiselect();
    private final ComboBoxMultiselect expositions = new ComboBoxMultiselect();
    private final ComboBoxMultiselect audience = new ComboBoxMultiselect();
    private final PopupButton authors = new PopupButton();
    private final Panel results = new Panel();
    private final TextField sampleCount = new TextField();
    private final TextField wordCount = new TextField();

    @Autowired
    private SearchableTablePanel searchAuthorsPanel;

    @Override
    public void initializeTab() {
        setCaption(provider.getProperty("view.tab.data.selection.title"));
        addComponent(initMainPanel());
        initializeResultPanel();
        initListeners();
    }

    private void initializeResultPanel() {
        results.setCaption(provider.getProperty("view.tab.data.selection.result.panel"));
        results.addStyleName(ChronoTheme.RESULT_PANEL);
        results.addStyleName(ValoTheme.PANEL_BORDERLESS);
        results.setSizeUndefined();
        results.setContent(initializeResultContentPanel());
        results.setVisible(false);
    }

    public void showLoadingIndicator(){
        mainPanelContent.removeComponent(results);
        mainPanelContent.addComponent(loadingIndicator);
    }

    public HorizontalLayout initMainPanel(){

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);

        mainPanelContent.setSizeUndefined();
        mainPanelContent.addComponent(initializeComboBoxes());

        mainPanelContent.addComponent(buttonBar);
        mainPanelContent.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);

        wrapper.addComponent(mainPanelContent);
        wrapper.setComponentAlignment(mainPanelContent, Alignment.MIDDLE_CENTER);

        return  wrapper;
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
        sampleCount.setCaption(provider.getProperty("label.sample.count"));
        wordCount.setCaption(provider.getProperty("label.word.count"));

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

        years.setCaption(provider.getProperty("label.years"));
        years.addStyleName(ValoTheme.COMBOBOX_TINY);
        years.setInputPrompt(provider.getProperty("label.all"));
        left.addComponent(years);

        titles.setCaption(provider.getProperty("label.title"));
        titles.addStyleName(ValoTheme.COMBOBOX_TINY);
        titles.setInputPrompt(provider.getProperty("label.all"));
        left.addComponent(titles);

        HorizontalLayout authorWrapper = new HorizontalLayout();
        authorWrapper.setCaption(provider.getProperty("label.author"));
        authors.addStyleName(ValoTheme.BUTTON_TINY);
        authors.addStyleName(ChronoTheme.POPUP_BUTTON);
        authors.setContent(searchAuthorsPanel);
        authors.setWidth(160, Unit.PIXELS);
        authors.setCaption(provider.getProperty("label.all"));
        authorWrapper.addComponent(authors);
        left.addComponent(authorWrapper);


        FormLayout right = new FormLayout();
        right.setSpacing(true);

        audience.setCaption(provider.getProperty("label.audience"));
        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        audience.setInputPrompt(provider.getProperty("label.all"));
        right.addComponent(audience);

        periods.setCaption(provider.getProperty("label.period.type"));
        periods.addStyleName(ValoTheme.COMBOBOX_TINY);
        periods.setInputPrompt(provider.getProperty("label.all"));
        right.addComponent(periods);

        expositions.setCaption(provider.getProperty("label.exposition"));
        expositions.addStyleName(ValoTheme.COMBOBOX_TINY);
        expositions.setInputPrompt(provider.getProperty("label.all"));
        right.addComponent(expositions);


        layout.addComponent(left);
        layout.addComponent(right);
        return  layout;
    }

    public void initListeners(){
       getClearButton().addClickListener(event -> {
           years.unselectAll();
           titles.unselectAll();
           expositions.unselectAll();
           periods.unselectAll();
       });
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
