package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sample")
@Data
public class Sample implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sampleSeq")
    @SequenceGenerator(name = "sampleSeq", sequenceName = "sample_id_seq")
    private Integer id;

    @Column(name = "journal_title")
    private String journalTitle;

    @Column(name = "article_title")
    private String articleTitle;

    @OneToMany(mappedBy = "sample")
    private List<Author> authors;

    private String lang;

    private String style;

    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    private Date date;

    private String period;

    private String status;

    private String medium;

    @Column(name = "file_name" ,columnDefinition = "text")
    private String fileName;

    @Column(name = "txt" ,columnDefinition = "text")
    private String txt;

    @Column(columnDefinition = "int2")
    private Integer exposition;

    @Column(name="wc", columnDefinition = "int2")
    private Integer wordCount;
}
