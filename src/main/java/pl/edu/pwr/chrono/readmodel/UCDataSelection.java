package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;

/**
 * Created by tnaskret on 05.02.16.
 */

@Service
public interface UCDataSelection {

    DataSelectionResult search(DataSelectionDTO dto);

}

