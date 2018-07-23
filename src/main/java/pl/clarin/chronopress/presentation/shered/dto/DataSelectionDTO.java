package pl.clarin.chronopress.presentation.shered.dto;

import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class DataSelectionDTO {

    private Set<Integer> years;
    private Set<String> titles;
    private Set<String> audience;
    private List<String> authors;
    private Set<String> periodicType;
    private Set<Integer> exposition;

}
