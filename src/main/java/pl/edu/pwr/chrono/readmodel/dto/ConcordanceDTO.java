package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConcordanceDTO {

    private String base;
    private String lemma;
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
}
