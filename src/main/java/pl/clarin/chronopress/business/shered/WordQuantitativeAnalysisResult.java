package pl.clarin.chronopress.business.shered;


import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import pl.clarin.chronopress.business.sample.control.AverageIntegerCalulator;

@Data
public class WordQuantitativeAnalysisResult {

    private boolean wordAverageCalculations = false;
    private boolean wordFrequencyCalculations = false;

    private Average word = new Average();
    private Map<Long, Long> wordFrequencyHistogram = new HashMap<>();
    
    public void setWordAverage(AverageIntegerCalulator avr){
        word.setAveragesLength(avr.getAverage());
        word.setStandardDeviation(avr.getStandardDeviation());
        word.setCoefficientOfVariation(avr.getCoefficientOfVariation());
        word.setSkewness(avr.getSkewness());
        word.setKurtosis(avr.getKurtoze());
    }
}
