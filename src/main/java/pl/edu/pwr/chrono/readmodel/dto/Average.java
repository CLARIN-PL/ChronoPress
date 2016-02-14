package pl.edu.pwr.chrono.readmodel.dto;

import com.google.common.collect.Maps;
import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Unit;

import java.util.Map;

@Data
public class Average {

    private Unit Unit;
    private double averagesLength = 0;
    private double standardDeviation = 0;
    private double coefficientOfVariation = 0;
    private double skewness = 0;
    private double kurtosis = 0;
    private Map<Long, Long> averageLengthHistogram = Maps.newConcurrentMap();
}
