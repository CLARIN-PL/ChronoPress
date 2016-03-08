package pl.edu.pwr.chrono.domain;


import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "audience")
@Data
public class Audience implements Identifiable<Long>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "audienceSeq")
    @SequenceGenerator(name = "audienceSeq", sequenceName = "audience_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "audience_tiles",
            joinColumns = @JoinColumn(name = "audience_id")
    )
    private Set<String> journaltitle;

}

