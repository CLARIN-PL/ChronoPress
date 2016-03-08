package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "lexical_field")
@Data
public class LexicalField implements Serializable {

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
    private List<String> lexicalnames;

}
