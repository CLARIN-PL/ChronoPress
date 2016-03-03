package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.hene.popupbutton.PopupButton;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.MultiColumnPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.SearchableTablePanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.results.ResultSlot;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringComponent
@ViewScope
public class DataSelectionPanel extends VerticalLayout {

    private final ComboBoxMultiselect years = new ComboBoxMultiselect();
    private final ComboBoxMultiselect titles = new ComboBoxMultiselect();
    private final ComboBoxMultiselect periods = new ComboBoxMultiselect();
    private final ComboBoxMultiselect expositions = new ComboBoxMultiselect();
    private final ComboBoxMultiselect audience = new ComboBoxMultiselect();
    private final PopupButton authors = new PopupButton();

    private final Label sampleCount = new Label();
    private final Label wordCount = new Label();
    @Autowired
    protected DbPropertiesProvider provider;
    private CssLayout results;
    private VerticalLayout loading;
    private MultiColumnPanel panel;

    @Autowired
    private SearchableTablePanel searchAuthorsPanel;

    @PostConstruct
    public void initialize() {
        setSpacing(true);
        panel = initSelectionPanel();
        addComponent(panel);

        results = buildResult();
        loading = initializeLoading();

        addComponent(results);
        setComponentAlignment(results, Alignment.TOP_CENTER);
    }

    private CssLayout buildResult() {

        CssLayout sparks = new CssLayout();
        sparks.setWidth(100, Unit.PERCENTAGE);
        sparks.addStyleName("sparks");
        Responsive.makeResponsive(sparks);

        sampleCount.setValue("0");
        wordCount.setValue("0");

        ResultSlot samples = new ResultSlot(provider.getProperty("label.sample.count"), sampleCount);
        ResultSlot words = new ResultSlot(provider.getProperty("label.word.count"), wordCount);

        sparks.addComponent(samples);
        sparks.addComponent(words);

        return sparks;
    }


    private MultiColumnPanel initSelectionPanel() {

        HorizontalLayout wrapper = new HorizontalLayout();
        authors.addStyleName(ValoTheme.BUTTON_TINY);
        authors.addStyleName(ChronoTheme.POPUP_BUTTON);
        authors.setContent(searchAuthorsPanel);
        authors.setWidth(160, Unit.PIXELS);
        wrapper.addComponent(authors);

        return new MultiColumnPanel
                .PanelBuilder(provider)
                .addColumnTitle(provider.getProperty("view.data.selection.title"))
                .addPanel(null, new MultiColumnPanel
                        .PanelBuilder
                        .ContentBuilder()
                        .addComponent(provider.getProperty("label.years"), years)
                        .addComponent(provider.getProperty("label.journal.title"), titles)
                        .addComponent(provider.getProperty("label.author"), wrapper)
                        .addComponent(provider.getProperty("label.audience"), audience)
                        .addComponent(provider.getProperty("label.period"), periods)
                        .addComponent(provider.getProperty("label.exposition"), expositions)
                        .buildWithFromLayout())
                .build();
    }

    public void showLoadingIndicator() {
        replaceComponent(results, loading);
    }

    public void showResults(DataSelectionResult result) {
        replaceComponent(loading, results);
        wordCount.setValue(Long.toString(result.getWordCount()));
        sampleCount.setValue(Long.toString(result.getSampleCount()));
    }

    public void reset() {
        years.unselectAll();
        titles.unselectAll();
        expositions.unselectAll();
        periods.unselectAll();
        audience.unselectAll();
        searchAuthorsPanel.clearSelection();
        sampleCount.setValue("0");
        wordCount.setValue("0");
    }

    public DataSelectionDTO getData() {
        DataSelectionDTO dto = new DataSelectionDTO();
        dto.setYears((Set<Integer>) years.getValue());
        dto.setTitles((Set<String>) titles.getValue());
        dto.setPeriodicType((Set<String>) periods.getValue());
        dto.setExposition((Set<Integer>) expositions.getValue());
        dto.setAuthors(searchAuthorsPanel.getSelectedItems());
        return dto;
    }

    private VerticalLayout initializeLoading() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);

        HorizontalLayout loadingNotification = new HorizontalLayout();
        loadingNotification.setSpacing(true);
        loadingNotification.addComponents(progressBar, new Label(provider.getProperty("label.loading")));

        layout.addComponents(loadingNotification);
        layout.setComponentAlignment(loadingNotification, Alignment.MIDDLE_CENTER);

        return layout;
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
