package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.calculations.boundary.CalculationsFacade;
import pl.clarin.chronopress.business.calculations.boundary.DataExplorationResult;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.business.shered.SentenceQuantitativeAnalysisResult;
import pl.clarin.chronopress.business.shered.WordQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.FrequencyList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.LexemeProfileList;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.NamesOnGoogleMap;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.SentenceAverageLengthHistogram;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ShowConcordanceWindowEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ShowSampleByIdEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.TimeSeriesChart;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.WordAverageLengthHistogram;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.WordZipfFrequencyHistogram;
import pl.clarin.chronopress.presentation.page.start.SearchAndShowConcordanceEvent;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@Slf4j
@UIScoped
public class DataAnalyseViewPresenter extends AbstractPresenter<DataAnalyseView> {

    @Inject
    SampleFacade sampleFacade;

    @Inject
    CalculationsFacade service;

    @Inject
    Instance<WordAverageLengthHistogram> averageLengthHistogram;

    @Inject
    Instance<WordZipfFrequencyHistogram> wordZipfFrequencyHistograms;

    @Inject
    Instance<SentenceAverageLengthHistogram> sentenceAverageLengthHistograms;

    @Inject
    Instance<LexemeProfileList> lexemeProfileList;

    @Inject
    Instance<FrequencyList> frequencyLists;

    @Inject
    Instance<ConcordanceList> concordanceLists;

    @Inject
    Instance<TimeSeriesChart> timeSeriesCharts;

    @Inject
    Instance<NamesOnGoogleMap> namesOnGoogleMaps;

    @Inject
    @Dedicated
    ExecutorService executor;

    @Override
    protected void onViewEnter() {
        getView().initDataAnalyseScreen(sampleFacade.getInitDataSelection());
        getView().showDataAnalyseScreen();
    }

    public void onShowAnalyseScreen(@Observes(notifyObserver = Reception.IF_EXISTS) ShowAnalyseScreenEvent event) {
        getView().showDataAnalyseScreen();
    }

    public void onCalculateWordQuantitive(@Observes(notifyObserver = Reception.IF_EXISTS) CalculateWordQuantitiveAnalysisEvent event) {
        getView().showLoading();
        CompletableFuture<WordQuantitativeAnalysisResult> future = CompletableFuture
                .supplyAsync(() -> service.wordQuantitiveAnalysis(event), executor).exceptionally(t -> {
            log.debug("Calculation error", t);
            getView().hideLoading();
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

    public void onCalculateSentenceQuantitive(@Observes(notifyObserver = Reception.IF_EXISTS) CalculateSentenceQuantitiveAnalysisEvent event) {
        getView().showLoading();
        CompletableFuture<SentenceQuantitativeAnalysisResult> future = CompletableFuture.supplyAsync(() -> service.sentenceQuantitiveAnalysis(event), executor);

        future.thenAccept((SentenceQuantitativeAnalysisResult result) -> {
            if (result.isSentenceAverageCalculations()) {
                SentenceAverageLengthHistogram r = sentenceAverageLengthHistograms.get();
                r.addData(result);
                getView().addResultPanel(r);
            }
        });
    }

    public void onConcordanceSearch(@Observes(notifyObserver = Reception.IF_EXISTS) SearchAndShowConcordanceEvent event) {
        getView().selectConcordance(event.getLemma());
    }

    public void onCalculateDataExploration(@Observes(notifyObserver = Reception.IF_EXISTS) CalculateDataExplorationEvent event) {
        getView().showLoading();
        CompletableFuture<DataExplorationResult> future = CompletableFuture.supplyAsync(() -> service.calculateDataExploration(event), executor);

        future.thenAccept((DataExplorationResult result) -> {
            if (result.getConcordance() != null) {
                ConcordanceList r = concordanceLists.get();
                r.addData(result.getConcordance());
                getView().addResultPanel(r);
            }
            if (result.getProfile() != null) {
                LexemeProfileList r = lexemeProfileList.get();
                r.addData(result.getProfile());
                getView().addResultPanel(r);
            }
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
            if (result.getGeolocations() != null) {
                NamesOnGoogleMap r = namesOnGoogleMaps.get();
                r.addData(result.getGeolocations());
                getView().addResultPanel(r);
            }
        });
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
