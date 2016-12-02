/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.calculations.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.clarin.chronopress.business.calculations.control.DataExploration;
import pl.clarin.chronopress.business.calculations.control.QuantitiveAnalysis;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.shered.SentenceQuantitativeAnalysisResult;
import pl.clarin.chronopress.business.shered.WordQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateDataExplorationEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateSentenceQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateTimeSerieEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateWordQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm.DataExplorationType;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.SimpleGeolocation;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesResult;

@Stateless
public class CalculationsFacade {

    @Inject
    QuantitiveAnalysis analysis;

    @Inject
    DataExploration dataExploration;

    @Inject
    SampleFacade facade;

    public WordQuantitativeAnalysisResult wordQuantitiveAnalysis(CalculateWordQuantitiveAnalysisEvent dto) {
        return analysis.wordQuantitiveAnalysis(dto);
    }

    public SentenceQuantitativeAnalysisResult sentenceQuantitiveAnalysis(CalculateSentenceQuantitiveAnalysisEvent event) {
        return analysis.sentenceQuantitiveAnalysis(event);
    }

    public TimeSeriesResult calculateTimeSeries(CalculateTimeSerieEvent event) {
        return facade.findTimeSeries(event.getDataSelectionDTO(), event.getTimeSeriesDTO());
    }

    public DataExplorationResult calculateDataExploration(CalculateDataExplorationEvent event) {
        DataExplorationResult result = new DataExplorationResult();

        DataExplorationType type = event.getDataExplorationDTO().getDataExplorationType();

        if (type.equals(DataExplorationType.LEXEME_CONCORDANCE)) {
            result.setConcordance(dataExploration.calculateConcordance(
                    event.getDataSelectionDTO(),
                    event.getDataExplorationDTO().getLemma(),
                    event.getDataExplorationDTO().getCaseSensitive()
            ));
        }
        if (type.equals(DataExplorationType.LEXEME_FREQUENCY_LIST)) {
            result.setWordFrequencyByLexeme(dataExploration.calculateWordFrequencyByLexeme(event.getDataSelectionDTO()));
        }
        if (type.equals(DataExplorationType.NOT_LEMMATIZED_FREQUENCY_LIST)) {
            result.setWordFrequencyNotLematized(dataExploration.calculateWordFrequencyNotLematized(event.getDataSelectionDTO()));
        }
        if (type.equals(DataExplorationType.PROFILE)) {
            result.setProfile(dataExploration.findLexemeProfiles(
                    event.getDataSelectionDTO(), event.getDataExplorationDTO().getLemma(),
                    event.getDataExplorationDTO().getContextPos(),
                    event.getDataExplorationDTO().getLeftContextGap(),
                    event.getDataExplorationDTO().getRightContextGap(),
                    event.getDataExplorationDTO().getCaseSensitive()
            ));
        }
        if (type.equals(DataExplorationType.PLACE_NAME_MAP)) {
            result.setGeolocations(findGeolocations(event.getDataSelectionDTO()));
        }

        return result;
    }

    public List<ConcordanceDTO> concordance(String lemma) {
        return dataExploration.calculateConcordance(lemma);
    }

    private List<SimpleGeolocation> findGeolocations(DataSelectionDTO dto) {
        return findCount(facade.findProperNames(dto));
    }

    private List<SimpleGeolocation> findCount(final List<SimpleGeolocation> list) {
        Map<SimpleGeolocation, Long> group = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<SimpleGeolocation> tmp = new ArrayList();
        group.forEach((geo, count) -> {
            geo.setFreq(count);
            tmp.add(geo);
        });
        return tmp;
    }

}
