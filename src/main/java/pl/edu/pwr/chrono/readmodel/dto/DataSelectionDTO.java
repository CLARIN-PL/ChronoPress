package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by tnaskret on 05.02.16.
 */
@Data
public class DataSelectionDTO {

    private List<Integer> years;
    private List<String> titles;
    private List<String> audience;
    private List<String> authors;
    private List<String> periodicType;
    private List<Integer> exposition;

}
