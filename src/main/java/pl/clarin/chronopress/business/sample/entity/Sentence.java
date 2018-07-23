package pl.clarin.chronopress.business.sample.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.Fetch;

import lombok.Data;

@Entity
@Table(name = "sentence",
        indexes = {
            @Index(name = "sentence_id_idx", columnList = "id"),
            @Index(name = "sentence_txt_idx", columnList = "sentence_txt"),
            @Index(name = "seq_idx", columnList = "seq"),
            @Index(name = "sentence_sample_idx", columnList = "sample_id"),
            @Index(name = "wc_idx", columnList = "wc")
        })
@Data
public class Sentence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sentence_txt", columnDefinition = "text")
    private String sentence;

    @Column(name = "wc")
    private Integer wordCount;

    private Integer seq;

    @ManyToOne()
    @JoinColumn(name = "sample_id")
    private Sample sample;

    @OneToMany(mappedBy = "sentence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Word> words;

    public Sentence() {
        this.setWords(new ArrayList<>());
        this.setSeq(0);
        this.setWordCount(0);
    }

}
