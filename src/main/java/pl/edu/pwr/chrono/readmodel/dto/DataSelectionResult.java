package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

@Data
public class DataSelectionResult {

    private Long sampleCount;
    private Long wordCount;

    public DataSelectionResult(Long sampleCount, Long wordCount) {
        this.sampleCount = sampleCount;
        this.wordCount = wordCount;
    }
}
