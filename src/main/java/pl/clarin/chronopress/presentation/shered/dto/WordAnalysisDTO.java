package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class WordAnalysisDTO {

    private Boolean allPartsOfSpeech = false;
    private Boolean adjective = false;
    private Boolean verb = false;
    private Boolean adverb = false;
    private Boolean noun = false;
    private Boolean namingUnit = false;
    private String wordRegularExpression = "";

    private Unit wordUnit;
    private Boolean wordAveragesLengthHistogram = true;
    private Boolean wordZipfHistogram = false;

}
