/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse;

import lombok.Getter;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;


public class CalculateTimeSerieEvent {
    
     @Getter
    private DataSelectionDTO dataSelectionDTO;
    
    @Getter
    private TimeSeriesDTO timeSeriesDTO;

    public CalculateTimeSerieEvent(DataSelectionDTO dataSelectionDTO, TimeSeriesDTO timeSeriesDTO) {
        this.dataSelectionDTO = dataSelectionDTO;
        this.timeSeriesDTO = timeSeriesDTO;
    }
}
