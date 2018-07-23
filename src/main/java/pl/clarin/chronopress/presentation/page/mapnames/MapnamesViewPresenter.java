package pl.clarin.chronopress.presentation.page.mapnames;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.calculations.boundary.DataExplorationResult;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.NamesOnGoogleMap;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ShowConcordanceWindowEvent;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class MapnamesViewPresenter extends AbstractPresenter<MapnamesView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<NamesOnGoogleMap> namesOnGoogleMaps;

    @Inject
    Instance<ConcordanceList> concordanceLists;

    @Inject
    SampleFacade sampleFacade;

    @Override
    protected void onViewEnter() {
        getView().setInitDataSelection(sampleFacade.getInitDataSelection());
    }

    public void onCalculateDataExploration(@Observes(notifyObserver = Reception.IF_EXISTS) CalculateDataExplorationEvent event) {

        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            NamesOnGoogleMap r = namesOnGoogleMaps.get();
            r.addData(result.getGeolocations());
            getView().addResultPanel(r);
        });
    }

    public void onShowConcordanceWindow(@Observes(notifyObserver = Reception.IF_EXISTS) ShowConcordanceWindowEvent event) {
        List<ConcordanceDTO> list = service.concordance(event.getBase(), event.getDate());
        ConcordanceList r = concordanceLists.get();
        r.addData(list);
        getView().showConcordanceWindow(r);
    }
}
