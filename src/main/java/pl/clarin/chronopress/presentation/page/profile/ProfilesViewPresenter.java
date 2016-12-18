package pl.clarin.chronopress.presentation.page.profile;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.calculations.boundary.DataExplorationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.LexemeProfileList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.TimeSeriesChart;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class ProfilesViewPresenter extends AbstractPresenter<ProfilesView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;
    
    @Inject
    Instance<TimeSeriesChart> timeSeriesCharts;

    @Inject
    Instance<LexemeProfileList> lexemeProfileList;
    
    @Override
    protected void onViewEnter() {
    }

     public void onCalculateDataExploration(CalculateDataExplorationEvent event) {

        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            if (result.getProfile() != null) {
                LexemeProfileList r = lexemeProfileList.get();
                r.addData(result.getProfile());
                getView().addResultPanel(r);
            }
        });
    }

}
