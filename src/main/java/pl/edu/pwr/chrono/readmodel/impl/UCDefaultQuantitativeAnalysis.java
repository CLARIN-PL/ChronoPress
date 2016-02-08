package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.UCQuantitativeAnalysis;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
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

    @Override
    public void calculate(DataSelectionResult result, QuantitativeAnalysisDTO dto) {

        List<Integer> ids = em.createNativeQuery(
                "select w.id from koper.sentence s " +
                        "inner join koper.word w on w.sentence_id = s.id " +
                        "where s.text_id in (?1)").setParameter(1, result.getSampleList()).getResultList();
        List<Word> filtered = wordRepository.findAll(WordSpecification.filter(ids, dto));
        System.out.println(filtered.size());
    }


}
