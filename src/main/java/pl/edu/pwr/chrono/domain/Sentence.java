package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by tnaskret on 05.02.16.
 */

@Entity
@Table(name = "sentence")
@Data
public class Sentence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sentenceSeq")
    @SequenceGenerator(name = "sentenceSeq", sequenceName = "sentence_id_seq")
    private Integer id;

    @Column(name = "shortid")
    private String shortId;

    @Column(name = "sent_tagged", columnDefinition = "text")
    private String  sentTagged;

    @Column(name = "sent_plain", columnDefinition = "text")
    private String sentPlain;

    @Column(name = "w_cnt")
    private Integer wordCount;

    private Integer seq;

    @ManyToOne
    @JoinColumn(name = "text_id", referencedColumnName = "id")
    private Text text;

}
