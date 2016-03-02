package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tnaskret on 02.03.16.
 */

@Entity
@Table(name = "page_aggregator")
@Data
public class PageAggregator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pageAggregatorSeq")
    @SequenceGenerator(name = "pageAggregatorSeq", sequenceName = "page_aggregator_id_seq")
    private Long id;
    private String title;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pageAggregator", cascade = CascadeType.ALL)
    private List<Page> pages;
}
