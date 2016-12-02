package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class SentenceWordCount {

    private Long sentenceId;
    private Integer wordCount;
    private Long letterCount;

    public SentenceWordCount(Long sentenceId, Integer wordCount, Long letterCount) {
        this.sentenceId = sentenceId;
        this.wordCount = wordCount;
        this.letterCount = letterCount;
    }
}
