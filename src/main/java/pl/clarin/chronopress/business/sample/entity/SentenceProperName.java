package pl.clarin.chronopress.business.sample.entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import pl.clarin.chronopress.business.propername.entity.ProperName;

@Entity
@Table(name = "Sentence_ProperName")
@Data
public class SentenceProperName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proper_name_id")
    private ProperName properName;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @Column(name = "occurence_count")
    private Integer occurenceCount;

}
