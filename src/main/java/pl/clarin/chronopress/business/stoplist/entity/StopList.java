package pl.clarin.chronopress.business.stoplist.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stop_list")
@Data
public class StopList {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
}
