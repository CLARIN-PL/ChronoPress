package pl.edu.pwr.chrono.application.service;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;

import java.util.List;
import java.util.Map;

/**
 * Created by tnaskret on 09.02.16.
 */
@Service
public interface UCCalculatingWordMeasurements {

    DefaultUCCalculatingWordMeasurements.Average calculate(List<Word> list,
                                                           DefaultUCCalculatingWordMeasurements.Unit unit);

    Map<Integer, Long> averageLengthHistogram(final List<Word> list);
    Map<Long, Long> frequencyHistogram(final List<Word> list);
}
