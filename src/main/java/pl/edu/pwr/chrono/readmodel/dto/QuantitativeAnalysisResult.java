package pl.edu.pwr.chrono.readmodel.dto;

import com.google.common.collect.Maps;
import lombok.Data;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;

import java.util.Map;

@Data
public class QuantitativeAnalysisResult {

    private boolean wordAverageCalculations = false;
    private boolean wordFrequencyCalculations = false;

    private Average word = new Average();
    private Map<Long, Long> wordFrequencyHistogram = Maps.newConcurrentMap();

    private boolean sentenceAverageCalculations = false;
    private Average sentence = new Average();

    public void setWordAverage(DefaultUCCalculatingWordMeasurements.Average avr){
        word.setAveragesLength(avr.getAverage());
        word.setStandardDeviation(avr.getStandardDeviation());
        word.setCoefficientOfVariation(avr.getCoefficientOfVariation());
        word.setKurtosis(avr.getKurtoze());
    }

    public void setSentenceAverage(DefaultUCCalculatingSentenceMeasurements.Average avr){
        sentence.setAveragesLength(avr.getAverage());
        sentence.setStandardDeviation(avr.getStandardDeviation());
        sentence.setCoefficientOfVariation(avr.getCoefficientOfVariation());
    }
}
