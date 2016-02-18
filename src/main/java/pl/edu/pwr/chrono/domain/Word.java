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

    @Column(name = "pos", columnDefinition = "text")
    private String partOfSpeech;

    @Column(name = "pos_class", columnDefinition = "text")
    private String posClass;

    @Column(name = "pos_alias", columnDefinition = "text")
    private String posAlias;

    @Column(name = "pos_lemma", columnDefinition = "text")
    private String posLemma;

    private Integer seq;

    @ManyToOne
    @JoinColumn(name = "sentence_id", referencedColumnName = "id")
    private Sentence sentence;

}
