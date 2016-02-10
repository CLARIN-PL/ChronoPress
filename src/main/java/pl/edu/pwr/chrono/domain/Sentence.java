package pl.edu.pwr.chrono.domain;

import lombok.Data;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by tnaskret on 05.02.16.
 */

@SqlResultSetMapping(
        name = "SentenceWordCountDTOMapping",
        classes = @ConstructorResult(
                targetClass = SentenceWordCount.class,
                columns = {
                        @ColumnResult(name = "sentence_id", type = Integer.class),
                        @ColumnResult(name = "word_count", type = Integer.class)}))

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
