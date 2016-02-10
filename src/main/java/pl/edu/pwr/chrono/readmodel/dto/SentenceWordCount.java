package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

/**
 * Created by tnaskret on 10.02.16.
 */
@Data
public class SentenceWordCount {

    private Integer sentenceId;
    private Integer wordCount;

    public SentenceWordCount(Integer sentenceId, Integer wordCount) {
        this.sentenceId = sentenceId;
        this.wordCount = wordCount;
    }
}
