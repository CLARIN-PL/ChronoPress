package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by tnaskret on 05.02.16.
 */
@Data
public class DataSelectionDTO {

    private Set<Integer> years;
    private Set<String> titles;
    private Set<String> audience;
    private List<String> authors;
    private Set<String> periodicType;
    private Set<Integer> exposition;

}
