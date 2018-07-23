package pl.clarin.chronopress.business.shered;

import lombok.Data;
import pl.clarin.chronopress.business.sample.control.AverageLongCalulator;

@Data
public class SentenceQuantitativeAnalysisResult {

    private boolean sentenceAverageCalculations = false;
    private Average sentence = new Average();

    public void setSentenceAverage(AverageLongCalulator avr) {
        sentence.setAveragesLength(avr.getAverage());
        sentence.setStandardDeviation(avr.getStandardDeviation());
        sentence.setCoefficientOfVariation(avr.getCoefficientOfVariation());
        sentence.setSkewness(avr.getSkewness());
    }
}
