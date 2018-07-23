package pl.clarin.chronopress.presentation.shered.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class TimeSeriesResult {

    private Time unit;
    private Map<String, List<TimeProbe>> timeSeries;
    private Map<String, List<TimeProbe>> movingAverage;

    public TimeSeriesResult(Time unit, Map<String, List<TimeProbe>> timeSeries) {
        this.unit = unit;
        this.timeSeries = timeSeries;
    }
}
