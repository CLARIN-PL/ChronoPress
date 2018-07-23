package pl.clarin.chronopress.presentation.page.frequency;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.calculations.boundary.DataExplorationResult;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.FrequencyList;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class FrequencyViewPresenter extends AbstractPresenter<FrequencyView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<FrequencyList> frequencyLists;

    @Inject
    SampleFacade sampleFacade;

    @Override
    protected void onViewEnter() {
        getView().setInitDataSelection(sampleFacade.getInitDataSelection());
    }

    public void onCalculateDataExploration(CalculateDataExplorationEvent event) {

        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            if (result.getWordFrequencyByLexeme() != null) {
                FrequencyList r = frequencyLists.get();
                r.addData(result.getWordFrequencyByLexeme());
                getView().addResultPanel(r);
            }
            if (result.getWordFrequencyNotLematized() != null) {
                FrequencyList r = frequencyLists.get();
                r.addData(result.getWordFrequencyNotLematized());
                getView().addResultPanel(r);
            }
        });
    }

}
