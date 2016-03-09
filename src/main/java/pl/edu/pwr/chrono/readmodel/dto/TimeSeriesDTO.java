package pl.edu.pwr.chrono.readmodel.dto;

import com.google.common.collect.Sets;
import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Time;

import java.util.Set;

@Data
public class TimeSeriesDTO {

    private Time unit;
    private Set<String> lexeme = Sets.newHashSet();
    private String regularExpression = "";
    private Boolean timeSeriesCalculation = false;
    private Boolean movingAverage = false;
    private Integer movingAverageWindowSize = 0;

    public Boolean canExecuteCalculation() {
        return (((lexeme != null) && (lexeme.size() > 0)) || !"".equals(regularExpression)) && timeSeriesCalculation;
    }
}
