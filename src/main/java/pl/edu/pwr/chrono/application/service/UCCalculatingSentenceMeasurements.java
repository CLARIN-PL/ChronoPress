package pl.edu.pwr.chrono.application.service;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.List;
import java.util.Map;

/**
 * Created by tnaskret on 10.02.16.
 */
@Service
public interface UCCalculatingSentenceMeasurements {

    DefaultUCCalculatingSentenceMeasurements.Average calculateByWord(final List<SentenceWordCount> list);
    Map<Integer, Long> frequencyCalculationsByWord(final List<SentenceWordCount> list);
}
