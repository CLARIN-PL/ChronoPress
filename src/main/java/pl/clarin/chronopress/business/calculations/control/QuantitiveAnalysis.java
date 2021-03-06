package pl.clarin.chronopress.business.calculations.control;

import com.google.gwt.thirdparty.guava.common.collect.Maps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Column;

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

            List<Integer> words = new ArrayList<>();

            AverageIntegerCalulator averageCalulator = null;

            if (dto.getWordAnalysisDTO().getWordAveragesLengthHistogram()) {

                if (dto.getWordAnalysisDTO().getWordUnit() == Unit.LETTER) {
                    words = sampleFacade.findWordsLengths("letterCount", dto.getDataSelectionDTO(), dto.getWordAnalysisDTO());
                }
                if (dto.getWordAnalysisDTO().getWordUnit() == Unit.FONEM) {
                    words = sampleFacade.findWordsLengths("fonemCount", dto.getDataSelectionDTO(), dto.getWordAnalysisDTO());
                }

                if (dto.getWordAnalysisDTO().getWordUnit() == Unit.SYLLABLE) {
                    words = sampleFacade.findWordsLengths("syllableCount", dto.getDataSelectionDTO(), dto.getWordAnalysisDTO());
                }

                averageCalulator = averageCalculations(words);
                result.getWord().setUnit(dto.getWordAnalysisDTO().getWordUnit());

                result.setWordAverageCalculations(true);
                result.setWordAverage(averageCalulator);
                result.getWord().setAverageLengthHistogram(averageLengthHistogram(words));
            }

            if (dto.getWordAnalysisDTO().getWordZipfHistogram()) {
                result.setWordFrequencyCalculations(true);
                result.setWordFrequencyHistogram(frequencyZipfHistogram(sampleFacade.findWordsLengthFrequency(dto.getDataSelectionDTO(), dto.getWordAnalysisDTO())));
            }
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
            if (dto.getAnalysisDTO().getSentenceUnit() == Unit.FONEM) {
                result.getSentence().setUnit(Unit.FONEM);
                calculateSentence(Unit.FONEM, result, list);
            }
            if (dto.getAnalysisDTO().getSentenceUnit() == Unit.SYLLABLE) {
                result.getSentence().setUnit(Unit.SYLLABLE);
                calculateSentence(Unit.SYLLABLE, result, list);
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
        if (unit == Unit.SYLLABLE) {
            return list.stream()
                    .map(SentenceWordCount::getSyllableCount)
                    .collect(AverageLongCalulator::new, AverageLongCalulator::accept, AverageLongCalulator::combine);
        }
        if (unit == Unit.FONEM) {
            return list.stream()
                    .map(SentenceWordCount::getFonemCount)
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
        if (unit == Unit.SYLLABLE) {
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(s -> s.getSyllableCount(),
                            Collectors.counting()));
            return sortByLongKey(map);
        }
        if (unit == Unit.FONEM) {
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(s -> s.getFonemCount(),
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
