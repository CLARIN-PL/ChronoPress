package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.layout.SearchableTablePanel;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class DataSelectionForm extends CustomComponent {

    private final ComboBoxMultiselect years = new ComboBoxMultiselect();
    private final ComboBoxMultiselect titles = new ComboBoxMultiselect();
    private final ComboBoxMultiselect periods = new ComboBoxMultiselect();
    private final ComboBoxMultiselect expositions = new ComboBoxMultiselect();
    private final ComboBoxMultiselect audience = new ComboBoxMultiselect();
    private final PopupButton authors = new PopupButton();

    @Inject
    DbPropertiesProvider provider;

    @Inject
    SearchableTablePanel searchAuthorsPanel;

    @PostConstruct
    public void initialize() {

        authors.addStyleName(ValoTheme.BUTTON_TINY);
        authors.addStyleName(ChronoTheme.POPUP_BUTTON);
        authors.setContent(searchAuthorsPanel);
        authors.setWidth(160, Unit.PIXELS);

        years.setCaption(provider.getProperty("label.years"));
        years.addStyleName(ValoTheme.COMBOBOX_TINY);
        titles.setCaption(provider.getProperty("label.journal.title"));
        titles.addStyleName(ValoTheme.COMBOBOX_TINY);
        audience.setCaption(provider.getProperty("label.audience"));
        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        periods.setCaption(provider.getProperty("label.period"));
        periods.addStyleName(ValoTheme.COMBOBOX_TINY);
        expositions.setCaption(provider.getProperty("label.exposition"));
        expositions.addStyleName(ValoTheme.COMBOBOX_TINY);

        VerticalLayout layout = new MVerticalLayout()
                .withMargin(false)
                .withSpacing(false)
                .with(buildSelectionPanel());

        setSizeUndefined();
        setCompositionRoot(layout);
    }

    private HorizontalLayout buildSelectionPanel() {

        HorizontalLayout wrapper = new MHorizontalLayout()
                .withCaption(provider.getProperty("label.author"))
                .with(authors);

        FormLayout left = new MFormLayout(years, titles, wrapper)
                .withStyleName(ChronoTheme.COMPACT_FORM);
        FormLayout right = new MFormLayout(audience, periods, expositions)
                .withStyleName(ChronoTheme.COMPACT_FORM);

        return new MHorizontalLayout()
                .with(left, right)
                .withMargin(false);
    }

    public void reset() {
        years.unselectAll();
        titles.unselectAll();
        expositions.unselectAll();
        periods.unselectAll();
        audience.unselectAll();
        searchAuthorsPanel.clearSelection();
    }

    public DataSelectionDTO getData() {
        DataSelectionDTO dto = new DataSelectionDTO();
        dto.setYears((Set<Integer>) years.getValue());
        dto.setTitles((Set<String>) titles.getValue());
        dto.setPeriodicType((Set<String>) periods.getValue());
        dto.setExposition((Set<Integer>) expositions.getValue());
        if (audience.getValue() != null && ((Set<String>) audience.getValue()).size() > 0) {
            dto.setAudience((Set<String>) audience.getValue());
        }
        dto.setAuthors(searchAuthorsPanel.getSelectedItems());
        return dto;
    }

    public void setAudience(List<String> list) {
        audience.addItems(list);
    }

    public void setAuthors(List<String> list) {
        searchAuthorsPanel.populateContainer(list);
    }

    public void setYears(List<Integer> list) {
        years.addItems(list);
    }

    public void setPeriods(List<String> list) {
        periods.addItems(list);
    }

    public void setTiles(List<String> list) {
        titles.addItems(list);
    }

    public void setExposition(List<Integer> list) {
        expositions.addItems(list);
    }
}
