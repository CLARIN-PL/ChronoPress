package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;
import pl.edu.pwr.chrono.webui.ui.dataanalyse.DataExplorationTab;

/**
 * Created by tnaskret on 18.02.16.
 */
@Data
public class DataExplorationDTO {

    private String lemma = "";
    private DataExplorationTab.DataExplorationType dataExplorationType;

}