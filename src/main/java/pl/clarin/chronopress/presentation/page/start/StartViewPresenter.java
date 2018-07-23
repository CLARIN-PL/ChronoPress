package pl.clarin.chronopress.presentation.page.start;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.calculations.boundary.DataExplorationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.shered.dto.DataExplorationDTO;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class StartViewPresenter extends AbstractPresenter<StartView> {

    @Inject
    javax.enterprise.event.Event<SearchAndShowConcordanceEvent> sasEvent;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<ConcordanceList> concordanceLists;

    @Override
    protected void onViewEnter() {
        getView().onViewEnter();
    }

    public void onSearch(String lemma) {

        DataExplorationDTO dataExplorationDTO = new DataExplorationDTO();
        dataExplorationDTO.setLemma(lemma);
        dataExplorationDTO.setDataExplorationType(DataExplorationForm.DataExplorationType.LEXEME_CONCORDANCE);

        CalculateDataExplorationEvent event = new CalculateDataExplorationEvent(new DataSelectionDTO(), dataExplorationDTO);

        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            if (result.getConcordance() != null) {
                ConcordanceList r = concordanceLists.get();
                r.addData(result.getConcordance());
                getView().addResultPanel(r);
            }
        });
    }
}
