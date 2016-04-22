package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "proper_names")
@Data
public class ProperName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "proper_namesSeq")
    @SequenceGenerator(name = "proper_namesSeq", sequenceName = "proper_names_id_seq")
    private Long id;

    @Column(name = "original_proper_name")
    private String originalProperName;

    private String alias;

    private String type;

    private float  lat;

    private float  lon;

    private boolean processed;
}
