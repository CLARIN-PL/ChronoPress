package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authorSeq")
    @SequenceGenerator(name = "authorSeq", sequenceName = "author_id_seq")
    private Integer id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "sample_id", referencedColumnName = "id")
    private Sample sample;

}
