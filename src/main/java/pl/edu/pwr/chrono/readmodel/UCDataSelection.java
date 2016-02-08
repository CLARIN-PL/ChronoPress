package pl.edu.pwr.chrono.readmodel;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;

import java.util.Optional;

/**
 * Created by tnaskret on 05.02.16.
 */

@Service
public interface UCDataSelection {

    ListenableFuture<Optional<DataSelectionResult>> search(DataSelectionDTO dto);

}

