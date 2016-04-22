package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property")
@Data
public class Property implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "propertySeq")
    @SequenceGenerator(name = "propertySeq", sequenceName = "property_id_seq")
    private Long id;

    private String key;
    private String value;
    private String lang;

}
