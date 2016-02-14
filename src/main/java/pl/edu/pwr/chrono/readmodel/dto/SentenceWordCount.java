package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

@Data
public class SentenceWordCount {

    private Integer sentenceId;
    private Long wordCount;
    private Long letterCount;

    public SentenceWordCount(Integer sentenceId, Long wordCount, Long letterCount) {
        this.sentenceId = sentenceId;
        this.wordCount = wordCount;
        this.letterCount = letterCount;
    }
}
