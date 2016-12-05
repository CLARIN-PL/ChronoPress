package pl.clarin.chronopress.presentation.page.timeseries;

import com.vaadin.cdi.UIScoped;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class TimeSeriesViewPresenter extends AbstractPresenter<TimeSeriesView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Override
    protected void onViewEnter() {
    }

}
