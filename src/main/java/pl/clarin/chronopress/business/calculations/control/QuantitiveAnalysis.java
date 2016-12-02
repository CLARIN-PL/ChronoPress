/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.calculations.control;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.control.AverageIntegerCalulator;
import pl.clarin.chronopress.business.sample.control.AverageLongCalulator;
import pl.clarin.chronopress.business.shered.SentenceQuantitativeAnalysisResult;
import pl.clarin.chronopress.business.shered.WordQuantitativeAnalysisResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateSentenceQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateWordQuantitiveAnalysisEvent;
import pl.clarin.chronopress.presentation.shered.dto.SentenceWordCount;
import pl.clarin.chronopress.presentation.shered.dto.Unit;

@Stateless
public class QuantitiveAnalysis {

    @Inject
    SampleFacade sampleFacade;

    public WordQuantitativeAnalysisResult wordQuantitiveAnalysis(CalculateWordQuantitiveAnalysisEvent dto) {

        final WordQuantitativeAnalysisResult result = new WordQuantitativeAnalysisResult();

        if (dto.getWordAnalysisDTO().getWordAveragesLengthHistogram()
                || dto.getWordAnalysisDTO().getWordZipfHistogram()) {

            List<Integer> words = sampleFacade.findWordsLengths(dto.getDataSelectionDTO(), dto.getWordAnalysisDTO());
            AverageIntegerCalulator averageCalulator = averageCalculations(words);

            if (dto.getWordAnalysisDTO().getWordAveragesLengthHistogram()) {
                result.setWordAverageCalculations(true);
                result.setWordAverage(averageCalulator);
                result.getWord().setUnit(Unit.LETTER);
                result.getWord().setAverageLengthHistogram(averageLengthHistogram(words));
            }
        }

        if (dto.getWordAnalysisDTO().getWordZipfHistogram()) {
            result.setWordFrequencyCalculations(true);
            result.setWordFrequencyHistogram(frequencyZipfHistogram(sampleFacade.findWordsLengthFrequency(dto.getDataSelectionDTO(), dto.getWordAnalysisDTO())));
        }

        return result;

    }

    public SentenceQuantitativeAnalysisResult sentenceQuantitiveAnalysis(CalculateSentenceQuantitiveAnalysisEvent dto) {

        final SentenceQuantitativeAnalysisResult result = new SentenceQuantitativeAnalysisResult();

        if (dto.getAnalysisDTO().getSentenceAverageLengthHistogram()) {
            List<SentenceWordCount> list = sampleFacade.findSentenceWordCountAndWordLength(dto.getDataSelectionDTO(), dto.getAnalysisDTO());
            if (dto.getAnalysisDTO().getSentenceUnit() == Unit.WORD) {
                result.getSentence().setUnit(Unit.WORD);
                calculateSentence(Unit.WORD, result, list);
            }
            if (dto.getAnalysisDTO().getSentenceUnit() == Unit.LETTER) {
                result.getSentence().setUnit(Unit.LETTER);
                calculateSentence(Unit.LETTER, result, list);
            }
        }
        return result;
    }

    private void calculateSentence(Unit unit, SentenceQuantitativeAnalysisResult result, List<SentenceWordCount> list) {
        AverageLongCalulator average = averageCalculations(list, unit);
        result.setSentenceAverage(average);
        result.setSentenceAverageCalculations(true);
        result.getSentence().setAverageLengthHistogram(frequencyCalculations(list, unit));
    }

    private AverageLongCalulator averageCalculations(final List<SentenceWordCount> list, Unit unit) {
        if (unit == Unit.WORD) {
            return list.stream()
                    .map(SentenceWordCount::getWordCount)
                    .collect(AverageLongCalulator::new, AverageLongCalulator::accept, AverageLongCalulator::combine);
        }
        if (unit == Unit.LETTER) {
            return list.stream()
                    .map(SentenceWordCount::getLetterCount)
                    .collect(AverageLongCalulator::new, AverageLongCalulator::accept, AverageLongCalulator::combine);
        }
        return new AverageLongCalulator();
    }

    public Map frequencyCalculations(final List<SentenceWordCount> list, Unit unit) {

        if (unit == Unit.WORD) {
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(s -> s.getWordCount().longValue(), Collectors.counting()));
            return sortByLongKey(map);
        }
        if (unit == Unit.LETTER) {
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(s -> s.getLetterCount().longValue(),
                            Collectors.counting()));
            return sortByLongKey(map);
        }
        return Maps.newConcurrentMap();
    }

    public Map averageLengthHistogram(final List<Integer> list) {
        Map<Long, Long> map = list.stream()
                .collect(Collectors.groupingBy(o -> o.longValue(), Collectors.counting()));
        return sortByLongKey(map);
    }

    public Map frequencyZipfHistogram(final List<Long> list) {
        Map<Long, Long> result = list.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        return sortByLongKey(result);
    }

    public static Map sortByLongKey(Map<Long, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private AverageIntegerCalulator averageCalculations(final List<Integer> list) {
        return list.stream()
                .collect(AverageIntegerCalulator::new, AverageIntegerCalulator::accept, AverageIntegerCalulator::combine);
    }
}
