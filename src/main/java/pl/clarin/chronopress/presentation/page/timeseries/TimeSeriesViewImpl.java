package pl.clarin.chronopress.presentation.page.timeseries;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.dataanalyse.TimeSeriesForm;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(TimeSeriesView.ID)
public class TimeSeriesViewImpl extends AbstractView<TimeSeriesViewPresenter> implements TimeSeriesView {

    @Inject
    private Instance<TimeSeriesViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    TimeSeriesForm timeSeriesForm;

    public TimeSeriesDTO getTimeSeriesDTO() {
        try {
            return timeSeriesForm.getTimeSeriesDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(TimeSeriesViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        timeSeriesForm.reset();
    }

    @PostConstruct
    public void init() {

        VerticalLayout content = new MVerticalLayout()
                .with(timeSeriesForm)
                .withStyleName(ChronoTheme.START_PANEL)
                .withMargin(true)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(content);
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

    @Override
    protected TimeSeriesViewPresenter generatePresenter() {
        return presenter.get();
    }
}
