package pl.edu.pwr.chrono.readmodel.dto;

import com.google.common.collect.Maps;
import lombok.Data;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;

import java.util.Map;

/**
 * Created by tnaskret on 08.02.16.
 */
@Data
public class QuantitativeAnalysisResult {

    private Boolean wordLetterUnit = false;
    private Boolean wordSyllableUnit = false;
    private double  wordAveragesLength = 0;
    private double  wordStandardDeviation = 0;
    private double  wordCoefficientOfVariation = 0;
    private double  wordSkewness = 0;
    private double  wordKurtosis = 0;
    private Map<Integer, Long> wordLengthFrequency = Maps.newConcurrentMap();

    private Boolean sentenceWordUnit = false;
    private Boolean sentenceLetterUnit = false;
    private double sentenceAverageLength = 0;
    private double sentenceStandardDeviation = 0;
    private double sentenceCoefficientOfVariation = 0;
    private double sentenceSkewness = 0;
    private Map<Integer, Long> sentenceEmpiricalDistributionLength = Maps.newConcurrentMap();

    public void setWordAverage(DefaultUCCalculatingWordMeasurements.Average avr){
        wordAveragesLength = avr.getAverage();
        wordStandardDeviation = avr.getStandardDeviation();
        wordCoefficientOfVariation = avr.getCoefficientOfVariation();
    }

    public void setSentenceAverage(DefaultUCCalculatingSentenceMeasurements.Average avr){
        sentenceAverageLength = avr.getAverage();
        sentenceStandardDeviation = avr.getStandardDeviation();
        sentenceCoefficientOfVariation = avr.getCoefficientOfVariation();
    }

}
