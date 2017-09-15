package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
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

        audience.addStyleName(ValoTheme.COMBOBOX_TINY);

        periods.setCaption(provider.getProperty("label.period"));
        periods.addStyleName(ValoTheme.COMBOBOX_TINY);

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

        final Label txt1 = new Label();

        txt1.setValue("Opcja ta pozwala wybrać kolekcje różnych periodyków, skierowane do konkretnych grup odbiorczych.</br>"
                + "Wybór grupy odboirczej automatycznie anuluje wskazanie tytułów w polu \"Tytuł periodyku\"");
        txt1.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt1);

        final Label txt2 = new Label();

        txt2.setValue("Ekspozycja tekstu jest jego miejscem w strukturze periodyku, odpowiadającym ważności.</br> "
                + "Przyjęto strukturę trzypoziomową (strona tytułowa, środek, ostatnia strona)");
        txt2.setContentMode(ContentMode.HTML);

        final VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.addComponent(txt2);

        final PopupView help1 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        final PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent2);

        FormLayout right = new MFormLayout(
                new MHorizontalLayout(audience, help1).withCaption(provider.getProperty("label.audience")),
                periods,
                new MHorizontalLayout(expositions, help2).withCaption(provider.getProperty("label.exposition")))
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
