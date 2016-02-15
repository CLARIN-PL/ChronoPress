package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "word")
@Data
public class Word implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "wordSeq")
    @SequenceGenerator(name = "wordSeq", sequenceName = "word_id_seq")
    private Integer id;

    @Column(name = "shortid")
    private String shortId;

    @Column(columnDefinition = "text")
    private String txt;

    @Column(columnDefinition = "text")
    private String pos;

    @Column(columnDefinition = "text")
    private String pos_class;

    @Column(columnDefinition = "text")
    private String pos_alias;

    @Column(columnDefinition = "text")
    private String pos_lemma;

    private Integer seq;

    @ManyToOne
    @JoinColumn(name = "sentence_id", referencedColumnName = "id")
    private Sentence sentence;

}
