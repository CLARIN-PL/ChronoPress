package pl.clarin.chronopress.business.shered;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import pl.clarin.chronopress.presentation.shered.dto.Unit;

@Data
public class Average {

    private Unit Unit;
    private double averagesLength = 0;
    private double standardDeviation = 0;
    private double coefficientOfVariation = 0;
    private double skewness = 0;
    private double kurtosis = 0;
    private Map<Long, Long> averageLengthHistogram = new ConcurrentHashMap<>();

}
