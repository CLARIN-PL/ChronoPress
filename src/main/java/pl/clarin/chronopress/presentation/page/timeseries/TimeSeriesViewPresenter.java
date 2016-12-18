package pl.clarin.chronopress.presentation.page.timeseries;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateTimeSerieEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.TimeSeriesChart;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesResult;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class TimeSeriesViewPresenter extends AbstractPresenter<TimeSeriesView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;
    
    @Inject
    Instance<TimeSeriesChart> timeSeriesCharts;

    @Override
    protected void onViewEnter() {
    }

    public void onCalculateTimeSeries(@Observes(notifyObserver = Reception.IF_EXISTS) CalculateTimeSerieEvent event) {
        //getView().showLoading();
        CompletableFuture<TimeSeriesResult> future = CompletableFuture.supplyAsync(() -> service.calculateTimeSeries(event), executor);

        future.thenAccept((TimeSeriesResult result) -> {

            TimeSeriesChart r = timeSeriesCharts.get();
            r.addData(result);
            getView().addResultPanel(r);

        });
    }

}
