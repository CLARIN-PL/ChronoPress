package pl.clarin.chronopress.presentation.page.concordance;

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
import pl.clarin.chronopress.business.sample.boundary.ConcordanceQueryService;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ShowConcordanceWindowEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ShowSampleByIdEvent;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class ConcordanceViewPresenter extends AbstractPresenter<ConcordanceView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<ConcordanceList> concordanceLists;

    @Inject
    SampleFacade sampleFacade;

    @Inject
    ConcordanceQueryService queryService;

    @Override
    protected void onViewEnter() {
        getView().setInitDataSelection(sampleFacade.getInitDataSelection());
    }

    public void onCalculateDataExploration(CalculateDataExplorationEvent event) {

        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            if (result.getConcordance() != null) {
                ConcordanceList r = concordanceLists.get();
                r.addData(result.getConcordance());
                getView().addResultPanel(r);
            }
        });

        queryService.findConcordanceByLemma(event.getDataExplorationDTO().getLemma());
    }

    public void onShowConcordanceWindow(@Observes(notifyObserver = Reception.IF_EXISTS) ShowConcordanceWindowEvent event) {
        List<ConcordanceDTO> list = service.concordance(event.getBase());
        ConcordanceList r = concordanceLists.get();
        r.addData(list);
        getView().showConcordanceWindow(r);
    }

    public void onShowSampleById(@Observes(notifyObserver = Reception.IF_EXISTS) ShowSampleByIdEvent event) {
        Sample s = sampleFacade.findById(event.getId());
        getView().showSampleWindow(s, event.getLemma());
    }
}
