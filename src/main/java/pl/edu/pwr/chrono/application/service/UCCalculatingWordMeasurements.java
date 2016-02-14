package pl.edu.pwr.chrono.application.service;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Unit;

import java.util.List;
import java.util.Map;

@Service
public interface UCCalculatingWordMeasurements {

    DefaultUCCalculatingWordMeasurements.Average calculate(List<Word> list, Unit unit);

    Map averageLengthHistogram(final List<Word> list);

    Map frequencyHistogram(final List<Word> list);
}
