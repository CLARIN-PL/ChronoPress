package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm;

@Data
public class DataExplorationDTO {

    private String lemma = "";
    private Integer leftContextGap = 1;
    private Integer rightContextGap = 1;
    private DataExplorationForm.PartOfSpeech contextPos;
    private DataExplorationForm.DataExplorationType dataExplorationType;
    private Boolean caseSensitive = false;

}
