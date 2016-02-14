package pl.edu.pwr.chrono.application.service;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.List;
import java.util.Map;

@Service
public interface UCCalculatingSentenceMeasurements {

    DefaultUCCalculatingSentenceMeasurements.Average calculate(final List<SentenceWordCount> list, Unit unit);

    Map frequencyCalculations(final List<SentenceWordCount> list, Unit unit);
}
