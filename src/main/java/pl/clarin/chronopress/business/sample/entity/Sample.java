package pl.clarin.chronopress.business.sample.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.clarin.chronopress.business.shered.AbstractEntity;

@Entity
@Table(name = "sample",
        indexes = {
            @Index(name = "sample_id_idx", columnList = "id"),
            @Index(name = "j_title_idx", columnList = "journal_title"),
            @Index(name = "a_title_idx", columnList = "article_title"),
            @Index(name = "a_title_idx", columnList = "authors"),
            @Index(name = "publication_date_idx", columnList = "publication_date"),
            @Index(name = "period_idx", columnList = "period")
        })
@Data
@Cacheable
@EqualsAndHashCode(callSuper = true)
public class Sample extends AbstractEntity {

    @Column(name = "journal_title")
    private String journalTitle;

    @Column(name = "article_title", columnDefinition = "text")
    private String articleTitle;

    @Column(name = "authors", columnDefinition = "text")
    private String authors;

    private String lang;

    private String style;

    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    private Date date;

    private String period;

    private String status;

    private String medium;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "txt", columnDefinition = "text")
    private String txt;

    @Column(columnDefinition = "int2")
    private Integer exposition;

    @Column(name = "wc", columnDefinition = "int2")
    private Integer wordCount;

    @Column(name = "processing_status")
    @Enumerated
    private ProcessingStatus processingStatus;

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Sentence> sentences;

    @Override
    public String toString(){
        return journalTitle;
    }
}
