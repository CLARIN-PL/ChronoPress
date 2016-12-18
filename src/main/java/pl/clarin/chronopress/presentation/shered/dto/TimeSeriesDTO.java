package pl.clarin.chronopress.presentation.shered.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class TimeSeriesDTO {

    private Time unit;
    private Set<String> lexeme = new HashSet<>();
    private String regularExpression = "";
    private Boolean timeSeriesCalculation = true;
    private Boolean movingAverage = false;
    private Integer movingAverageWindowSize = 3;
    private Boolean asSumOfResults = false;

    public Boolean canExecuteCalculation() {
        return (((lexeme != null) && (lexeme.size() > 0)) || !"".equals(regularExpression)) && timeSeriesCalculation;
    }
}
