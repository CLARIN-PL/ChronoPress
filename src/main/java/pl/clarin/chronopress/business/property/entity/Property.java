package pl.clarin.chronopress.business.property.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property")
@Data
public class Property implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String key;
    private String value;
    private String lang;

}
