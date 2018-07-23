package pl.clarin.chronopress.presentation.page.dataanalyse;

import lombok.Getter;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;

public class CalculateSentenceQuantitiveAnalysisEvent {

    @Getter
    private DataSelectionDTO dataSelectionDTO;

    @Getter
    private SentenceAnalysisDTO analysisDTO;

    public CalculateSentenceQuantitiveAnalysisEvent(DataSelectionDTO dataSelectionDTO, SentenceAnalysisDTO sentenceDTO) {
        this.dataSelectionDTO = dataSelectionDTO;
        this.analysisDTO = sentenceDTO;
    }
}
