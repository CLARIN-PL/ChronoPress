package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by tnaskret on 05.02.16.
 */
@Data
public class DataSelectionResult {

    private List<Integer> sampleList;
    private Integer sampleCount;
    private Integer wordCount;

    public DataSelectionResult(List<Integer> sampleList, Integer sampleCount, Integer wordCount){
        this.sampleList = sampleList;
        this.sampleCount = sampleCount;
        this.wordCount = wordCount;
    }
}
