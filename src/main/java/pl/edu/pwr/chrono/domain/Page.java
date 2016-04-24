package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "page")
@Data
public class Page implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pageSeq")
    @SequenceGenerator(name = "pageSeq", sequenceName = "page_id_seq")
    private Long id;

    private String title;
    private boolean published;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "page_aggregator_id", referencedColumnName = "id")
    private PageAggregator pageAggregator;

    private String category;
}
