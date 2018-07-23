package pl.clarin.chronopress.presentation.page.dataanalyse;

import lombok.Getter;
import pl.clarin.chronopress.presentation.shered.dto.DataExplorationDTO;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;

public class CalculateDataExplorationEvent {

    @Getter
    private DataSelectionDTO dataSelectionDTO;

    @Getter
    private DataExplorationDTO dataExplorationDTO;

    public CalculateDataExplorationEvent(DataSelectionDTO dataSelectionDTO, DataExplorationDTO dataExplorationDTO) {
        this.dataSelectionDTO = dataSelectionDTO;
        this.dataExplorationDTO = dataExplorationDTO;
    }
}
