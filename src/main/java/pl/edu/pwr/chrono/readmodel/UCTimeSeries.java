package pl.edu.pwr.chrono.readmodel;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;

@Service
public interface UCTimeSeries {

    ListenableFuture<TimeSeriesResult> calculate(DataSelectionDTO data, TimeSeriesDTO dto);
}
