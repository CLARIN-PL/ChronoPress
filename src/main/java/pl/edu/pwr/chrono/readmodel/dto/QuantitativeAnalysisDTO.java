package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

/**
 * Created by tnaskret on 08.02.16.
 */
@Data
public class QuantitativeAnalysisDTO {

    private Boolean allPartsOfSpeech =  false;
    private Boolean adjective = false;
    private Boolean verb = false;
    private Boolean adverb = false;
    private Boolean noun = false;
    private Boolean namingUnit = false;
    private Boolean regularExpression = false;
    private String expression = "";

    private Boolean wordLetterUnit = false;
    private Boolean wordSyllableUnit = false;
    private Boolean wordAveragesLength = false;
    private Boolean wordStandardDeviation = false;
    private Boolean wordCoefficientOfVariation = false;
    private Boolean wordSkewness = false;
    private Boolean wordKurtosis = false;
    private Boolean wordEmpiricalDistributionZipfHistogram = false;

    private Boolean sentenceWordUnit = false;
    private Boolean sentenceLetterUnit = false;
    private Boolean sentenceAverageLength = false;
    private Boolean sentenceStandardDeviation = false;
    private Boolean sentenceCoefficientOfVariation = false;
    private Boolean sentenceSkewness = false;
    private Boolean sentenceEmpiricalDistributionLength = false;

}
