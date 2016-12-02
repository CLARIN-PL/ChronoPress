/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse;

import lombok.Getter;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;

public class CalculateWordQuantitiveAnalysisEvent {
    
    @Getter
    private DataSelectionDTO dataSelectionDTO;
    
    @Getter
    private WordAnalysisDTO wordAnalysisDTO;

    public CalculateWordQuantitiveAnalysisEvent(DataSelectionDTO dataSelectionDTO, WordAnalysisDTO wordDTO) {
        this.dataSelectionDTO = dataSelectionDTO;
        this.wordAnalysisDTO = wordDTO;
    }
}
