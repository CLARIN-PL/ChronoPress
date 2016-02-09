package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.UCQuantitativeAnalysis;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.repository.WordRepository;
import pl.edu.pwr.chrono.repository.WordSpecification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by tnaskret on 08.02.16.
 */

@Service
public class UCDefaultQuantitativeAnalysis implements UCQuantitativeAnalysis {

    @Autowired
    private WordRepository wordRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UCCalculatingWordMeasurements ucCalculatingWordMeasurements;

    @Override
    public QuantitativeAnalysisResult calculate(DataSelectionResult selection, QuantitativeAnalysisDTO dto) {

        List<Word> words  = filter(selection, dto);

        QuantitativeAnalysisResult result = new QuantitativeAnalysisResult();

        if(doWordAverageCalculations(dto)){
            if(dto.getWordLetterUnit()){
                DefaultUCCalculatingWordMeasurements.Average average = ucCalculatingWordMeasurements.calculate(
                        words, DefaultUCCalculatingWordMeasurements.Unit.LETTER);
                result.setWordAverage(average);
                result.setWordLetterUnit(true);
            }
        }

        if(dto.getWordEmpiricalDistributionZipfHistogram()){
            result.setWordLengthFrequency(ucCalculatingWordMeasurements.frequencyCalculations(words));
        }

        return result;
    }

    private  List<Word> filter(DataSelectionResult selection, QuantitativeAnalysisDTO dto) {
        List<Integer> ids = em.createNativeQuery(
                "select w.id from koper.sentence s " +
                        "inner join koper.word w on w.sentence_id = s.id " +
                        "where s.text_id in (?1)").setParameter(1, selection.getSampleList()).getResultList();
        return wordRepository.findAll(WordSpecification.filter(ids, dto));
    }

    private Boolean doWordAverageCalculations(final QuantitativeAnalysisDTO dto){
        if(dto.getWordAveragesLength()
                || dto.getWordCoefficientOfVariation()
                || dto.getWordStandardDeviation()) return true;
        return false;
    }
}
