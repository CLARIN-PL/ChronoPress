package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConcordanceDTO {

    private String base;
    private String lemma;
    private String left;
    private String right;
    private String sentence;
    private Date publicationDate;
    private String journalTitle;

    public ConcordanceDTO(String base, String lemma, String sentence, Date publicationDate, String journalTitle) {
        this.base = base;
        this.lemma = lemma;
        this.sentence = sentence;
        this.publicationDate = publicationDate;
        this.journalTitle = journalTitle;
    }

    public ConcordanceDTO(ConcordanceDTO c) {
        this.base = c.getBase();
        this.lemma = c.getLemma();
        this.sentence = c.getSentence();
        this.publicationDate = c.getPublicationDate();
        this.journalTitle = c.getJournalTitle();
    }
}
