package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Time;

import java.util.Set;

@Data
public class TimeSeriesDTO {

    private Time unit;
    private Set<String> lexeme;
    private String regularExpression = "";
    private Boolean timeSeriesCalculation = false;

}
