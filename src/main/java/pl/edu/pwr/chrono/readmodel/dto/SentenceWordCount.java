package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

/**
 * Created by tnaskret on 10.02.16.
 */
@Data
public class SentenceWordCount {

    private Integer sentenceId;
    private Integer wordCount;
    private Integer letterCount;

    public SentenceWordCount(Integer sentenceId, Integer wordCount, Integer letterCount) {
        this.sentenceId = sentenceId;
        this.wordCount = wordCount;
        this.letterCount = letterCount;
    }
}
