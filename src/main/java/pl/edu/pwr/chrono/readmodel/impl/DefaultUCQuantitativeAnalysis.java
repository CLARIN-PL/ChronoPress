package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.UCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.UCQuantitativeAnalysis;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;
import pl.edu.pwr.chrono.repository.TextRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class DefaultUCQuantitativeAnalysis implements UCQuantitativeAnalysis {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UCCalculatingWordMeasurements ucCalculatingWordMeasurements;

    @Autowired
    private UCCalculatingSentenceMeasurements ucCalculatingSentenceMeasurements;

    @Autowired
    private ListeningExecutorService service;

    @Autowired
    private TextRepository repository;

    @Override
    public ListenableFuture<QuantitativeAnalysisResult> calculate(DataSelectionDTO selection, QuantitativeAnalysisDTO dto) {

        return service.submit(() -> {

            final QuantitativeAnalysisResult result = new QuantitativeAnalysisResult();

            if (dto.getWordAveragesLengthHistogram() || dto.getWordZipfHistogram()) {

                List<Word> words = repository.findWords(selection, dto);

                if (dto.getWordAveragesLengthHistogram()) {
                    result.setWordAverageCalculations(true);
                    if (dto.getWordUnit() == Unit.LETTER) {
                        DefaultUCCalculatingWordMeasurements.Average average = ucCalculatingWordMeasurements.calculate(words, Unit.LETTER);
                        result.setWordAverage(average);
                        result.getWord().setUnit(Unit.LETTER);
                        result.getWord().setAverageLengthHistogram(ucCalculatingWordMeasurements.averageLengthHistogram(words));
                    }
                }

                if (dto.getWordZipfHistogram()) {
                    result.setWordFrequencyCalculations(true);
                    result.setWordFrequencyHistogram(ucCalculatingWordMeasurements.frequencyHistogram(words));
                }

            } else if (dto.getSentenceAverageLengthHistogram()) {

                List<SentenceWordCount> list = repository.findSentenceWordCountAndWordLength(selection);
                if (dto.getSentenceUnit() == Unit.WORD) {
                    result.getSentence().setUnit(Unit.WORD);
                    calculateSentence(Unit.WORD, result, list);
                }
                if (dto.getSentenceUnit() == Unit.LETTER) {
                    result.getSentence().setUnit(Unit.LETTER);
                    calculateSentence(Unit.LETTER, result, list);
                }
            }

            return result;
        });
    }

    private void calculateSentence(Unit unit, QuantitativeAnalysisResult result, List<SentenceWordCount> list) {
        DefaultUCCalculatingSentenceMeasurements.Average average = ucCalculatingSentenceMeasurements.calculate(list, unit);
        result.setSentenceAverage(average);
        result.setSentenceAverageCalculations(true);
        result.getSentence().setAverageLengthHistogram(ucCalculatingSentenceMeasurements.frequencyCalculations(list, unit));
    }
}
