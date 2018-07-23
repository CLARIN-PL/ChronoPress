package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;

public class TimeSeriesTab extends CustomComponent {

    @Inject
    TimeSeriesForm timeSeriesForm;

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("view.tab.time.series.title"));

        HorizontalLayout layout = new MHorizontalLayout()
                .withFullWidth()
                .with(timeSeriesForm)
                .withAlign(timeSeriesForm, Alignment.TOP_CENTER);
        setCompositionRoot(layout);
    }

    public TimeSeriesDTO getTimeSeriesDTO() {
        try {
            return timeSeriesForm.getTimeSeriesDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(WordQuantitativeAnalysisTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        timeSeriesForm.reset();
    }
}
