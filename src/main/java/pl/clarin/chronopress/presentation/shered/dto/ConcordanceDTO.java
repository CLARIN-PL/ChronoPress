package pl.clarin.chronopress.presentation.shered.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ConcordanceDTO {

    private Long textId;
    private String word;
    private String lemma;
    private String left;
    private String right;
    private String sentence;
    private Date publicationDate;
    private String journalTitle;
    private String articleTitle;
    private String style;
    private String period;
    private String status;

    public ConcordanceDTO(Long textId,String word, String lemma, String sentence, Date publicationDate, String journalTitle,
                          String articleTitle, String style, String period, String status) {
        this.textId = textId;
        this.word = word;
        this.lemma = lemma;
        this.sentence = sentence;
        this.publicationDate = publicationDate;
        this.journalTitle = journalTitle;
        this.articleTitle = articleTitle;
        this.style = style;
        this.period = period;
        this.status = status;
    }

    public ConcordanceDTO(ConcordanceDTO c) {
        this.textId = c.textId;
        this.word = c.getWord();
        this.lemma = c.getLemma();
        this.sentence = c.getSentence();
        this.publicationDate = c.getPublicationDate();
        this.journalTitle = c.getJournalTitle();
        this.articleTitle = c.getArticleTitle();
        this.style = c.getStyle();
        this.period = c.getPeriod();
        this.status = c.getStatus();
    }
}
