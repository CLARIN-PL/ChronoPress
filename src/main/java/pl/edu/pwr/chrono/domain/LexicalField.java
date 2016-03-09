package pl.edu.pwr.chrono.domain;

import com.google.common.collect.Sets;
import lombok.Data;
import pl.edu.pwr.chrono.infrastructure.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "lexical_field")
@Data
public class LexicalField implements Serializable, Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lexicalSeq")
    @SequenceGenerator(name = "lexicalSeq", sequenceName = "lexical_field_id_seq")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "lexical_names",
            joinColumns = @JoinColumn(name = "lexical_field_id")
    )
    private Set<String> lexicalnames = Sets.newHashSet();

}
