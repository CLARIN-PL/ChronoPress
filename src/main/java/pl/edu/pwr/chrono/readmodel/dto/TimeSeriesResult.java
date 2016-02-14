package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Time;

import java.util.List;
import java.util.Map;

@Data
public class TimeSeriesResult {

    private Time unit;
    private Map<String, List<TimeProbe>> timeSeries;

    public TimeSeriesResult(Time unit, Map<String, List<TimeProbe>> timeSeries) {
        this.unit = unit;
        this.timeSeries = timeSeries;
    }

}
