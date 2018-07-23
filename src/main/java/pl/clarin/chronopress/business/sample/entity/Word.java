package pl.clarin.chronopress.business.sample.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "word",
        indexes = {
            @Index(name = "word_id_idx", columnList = "id"),
            @Index(name = "word_txt_idx", columnList = "word"),
            @Index(name = "pos_idx", columnList = "pos"),
            @Index(name = "pos_alias_idx", columnList = "pos_alias"),
            @Index(name = "pos_lemma_idx", columnList = "pos_lemma"),
            @Index(name = "word_letter_cnt_idx", columnList = "letter_count"),
            @Index(name = "word_sentence_idx", columnList = "sentence_id")
        })
@Data
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "text")
    private String word;

    @Column(name = "pos")
    private String partOfSpeach;

    @Column(name = "pos_alias")
    private String posAlias;

    @Column(name = "pos_lemma", columnDefinition = "text")
    private String lemma;

    private Integer seq = 0;

    @Column(name = "letter_count")
    private Integer letterCount = 0;

    @Column(name = "syllable_count")
    private Integer syllableCount = 0;

    @Column(name = "fonem_count")
    private Integer fonemCount = 0;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

}
