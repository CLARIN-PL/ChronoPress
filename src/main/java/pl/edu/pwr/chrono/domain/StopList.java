package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "stop_list")
@Data
public class StopList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stop_listSeq")
    @SequenceGenerator(name = "stop_listSeq", sequenceName = "stop_list_id_seq")
    private Long id;

    private String name;
}
