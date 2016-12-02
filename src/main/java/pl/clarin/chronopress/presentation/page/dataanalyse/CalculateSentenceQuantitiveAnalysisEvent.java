/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
