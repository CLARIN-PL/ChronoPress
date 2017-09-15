package pl.clarin.chronopress.presentation.page.quantity;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.shered.SentenceQuantitativeAnalysisResult;
import pl.clarin.chronopress.business.shered.WordQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateSentenceQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateWordQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.SentenceAverageLengthHistogram;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.WordAverageLengthHistogram;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.WordZipfFrequencyHistogram;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class QuantityViewPresenter extends AbstractPresenter<QuantityView> {

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<WordAverageLengthHistogram> averageLengthHistogram;

    @Inject
    Instance<WordZipfFrequencyHistogram> wordZipfFrequencyHistograms;

    @Inject
    Instance<SentenceAverageLengthHistogram> sentenceAverageLengthHistograms;

    @Inject
    SampleFacade sampleFacade;

    @Override
    protected void onViewEnter() {
        getView().setInitDataSelection(sampleFacade.getInitDataSelection());
    }

    public void onCalculateWordQuantitive(DataSelectionDTO dataSelectionDTO, WordAnalysisDTO wordDTO) {
        CalculateWordQuantitiveAnalysisEvent event = new CalculateWordQuantitiveAnalysisEvent(dataSelectionDTO, wordDTO);
        CompletableFuture<WordQuantitativeAnalysisResult> future = CompletableFuture
                .supplyAsync(() -> service.wordQuantitiveAnalysis(event), executor).exceptionally(t -> {
            return null;
        });
        future.thenAccept((WordQuantitativeAnalysisResult result) -> {
            if (result.isWordAverageCalculations()) {
                WordAverageLengthHistogram r = averageLengthHistogram.get();
                r.addData(result);
                getView().addResultPanel(r);
            }
            if (result.isWordFrequencyCalculations()) {
                WordZipfFrequencyHistogram r = wordZipfFrequencyHistograms.get();
                r.addData(result);
                getView().addResultPanel(r);
            }
        });
    }

    public void onCalculateSentenceQuantitive(DataSelectionDTO dataSelectionDTO, SentenceAnalysisDTO sentenceDTO) {
        CalculateSentenceQuantitiveAnalysisEvent event = new CalculateSentenceQuantitiveAnalysisEvent(dataSelectionDTO, sentenceDTO);
        CompletableFuture<SentenceQuantitativeAnalysisResult> future = CompletableFuture.supplyAsync(() -> service.sentenceQuantitiveAnalysis(event), executor);

        future.thenAccept((SentenceQuantitativeAnalysisResult result) -> {
            if (result.isSentenceAverageCalculations()) {
                SentenceAverageLengthHistogram r = sentenceAverageLengthHistograms.get();
                r.addData(result);
                getView().addResultPanel(r);
            }
        });
    }

}
