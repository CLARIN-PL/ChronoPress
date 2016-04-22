package pl.edu.pwr.chrono.domain;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sentence_proper_name")
@Data
public class SentenceProperName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sentence_proper_nameSeq")
    @SequenceGenerator(name = "sentence_proper_nameSeq", sequenceName = "sentence_proper_name_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sentence_id", referencedColumnName = "id")
    private Sentence sentence;

    @ManyToOne
    @JoinColumn(name = "proper_name_id", referencedColumnName = "id")
    private ProperName properName;

    public SentenceProperName(){};
    public SentenceProperName(Sentence se, ProperName properName){
        this.sentence = se;
        this.properName = properName;
    };

}
