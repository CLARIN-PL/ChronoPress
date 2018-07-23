package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class SentenceAnalysisDTO {

    private Unit sentenceUnit;
    private String sentenceRegularExpression = "";
    private Boolean sentenceAverageLengthHistogram = true;

}
