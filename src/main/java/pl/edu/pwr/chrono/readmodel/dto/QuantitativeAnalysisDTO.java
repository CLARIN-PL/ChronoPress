package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Unit;

@Data
public class QuantitativeAnalysisDTO {

    private Boolean allPartsOfSpeech =  false;
    private Boolean adjective = false;
    private Boolean verb = false;
    private Boolean adverb = false;
    private Boolean noun = false;
    private Boolean namingUnit = false;
    private String wordRegularExpression = "";

    private Unit wordUnit;
    private Boolean wordAveragesLengthHistogram = false;
    private Boolean wordZipfHistogram = false;

    private Unit sentenceUnit;
    private String sentenceRegularExpression = "";
    private Boolean sentenceAverageLengthHistogram = false;

}
