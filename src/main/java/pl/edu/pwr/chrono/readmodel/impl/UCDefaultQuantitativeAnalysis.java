package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.UCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.UCQuantitativeAnalysis;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;
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

    @Autowired
    private UCCalculatingSentenceMeasurements ucCalculatingSentenceMeasurements;

    @Override
    public QuantitativeAnalysisResult calculate(DataSelectionResult selection, QuantitativeAnalysisDTO dto) {

        List<List<Integer>> wordsIds  = findWordIdFromText(selection);

        QuantitativeAnalysisResult result = new QuantitativeAnalysisResult();

        if(dto.getWordLetterUnit()){
            List<Word> words = filterWords(wordsIds, dto);
            if(dto.getWordLetterUnit()){
                DefaultUCCalculatingWordMeasurements.Average average = ucCalculatingWordMeasurements.calculate(
                        words, DefaultUCCalculatingWordMeasurements.Unit.LETTER);
                result.setWordAverage(average);
                result.setWordLetterUnit(true);
            }

            if(dto.getWordEmpiricalDistributionZipfHistogram()){
                result.setWordLengthFrequency(ucCalculatingWordMeasurements.frequencyCalculations(words));
            }
        }

        if(dto.getSentenceWordUnit()){
            List<SentenceWordCount> list = unitWordSentenceCount(wordsIds);
            if(dto.getSentenceWordUnit()){
                DefaultUCCalculatingSentenceMeasurements.Average average =
                        ucCalculatingSentenceMeasurements.calculateByWord(list);
                result.setSentenceWordUnit(true);
                result.setSentenceAverage(average);
            }

            if(dto.getSentenceEmpiricalDistributionLength()){
                result.setSentenceEmpiricalDistributionLength(ucCalculatingSentenceMeasurements.frequencyCalculationsByWord(list));
            }

        }

        return result;
    }

    private List<List<Integer>> findWordIdFromText(DataSelectionResult selection){
        List<List<Integer>> partitioned = Lists.partition(selection.getSampleList(), 10);
                final List<List<Integer>> results = Lists.newArrayList();
                partitioned.forEach(i ->{
                    results.add(em.createNativeQuery(
                            "select w.id from koper.sentence s " +
                                    "inner join koper.word w on w.sentence_id = s.id " +
                                    "where s.text_id in (?1)").setParameter(1, i).getResultList());
                } );
        return results;
    }

    private  List<Word> filterWords(List<List<Integer>> wordsIds, QuantitativeAnalysisDTO dto) {
        List<List<Word>> partitioned = Lists.newArrayList();
        wordsIds.forEach(i -> {
            partitioned.add(wordRepository.findAll(WordSpecification.filter(i, dto)));
        });
        List<Word> result = Lists.newArrayList();
        partitioned.forEach(l -> { result.addAll(l);});
        return result;
    }

    private  List<SentenceWordCount> unitWordSentenceCount(List<List<Integer>> wordsIds){
        List<List<SentenceWordCount>> partitioned = Lists.newArrayList();
        wordsIds.forEach(i -> {
            partitioned.add(em.createNativeQuery(
                    "SELECT w.sentence_id, count(*) as word_count FROM  koper.word as w " +
                            "WHERE (w.pos_alias != 'punct' and w.id in (?1)) " +
                            "GROUP BY w.sentence_id", "SentenceWordCountDTOMapping")
                    .setParameter(1, i).getResultList());
        });
        List<SentenceWordCount> result = Lists.newArrayList();
        partitioned.forEach(l -> { result.addAll(l);});
        return result;
    }
}
