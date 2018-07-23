package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class SentenceWordCount {

    private Long sentenceId;
    private Integer wordCount;
    private Long letterCount;
    private Long fonemCount;
    private Long syllableCount;

    public SentenceWordCount(Long sentenceId, Integer wordCount, Long letterCount, Long fonemCount, Long syllableCount) {
        this.sentenceId = sentenceId;
        this.wordCount = wordCount;
        this.letterCount = letterCount;
        this.fonemCount = fonemCount;
        this.syllableCount = syllableCount;
    }
}
