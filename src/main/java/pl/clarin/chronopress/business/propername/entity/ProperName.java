package pl.clarin.chronopress.business.propername.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "proper_name",
        indexes = {
            @Index(name = "prop_name_id_idx", columnList = "id"),
            @Index(name = "prop_name_orth_idx", columnList = "orth"),
            @Index(name = "prop_name_base_idx", columnList = "base"),
            @Index(name = "prop_name_on_map_idx", columnList = "name_on_map"),
            @Index(name = "prop_name_type_idx", columnList = "type"),
            @Index(name = "prop_name_user_correction", columnList = "user_correction")
        })
@Data
public class ProperName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orth")
    private String orth;

    @Column(name = "base")
    private String base;

    @Column(name = "type")
    private String type;

    @Column(name = "user_correction")
    private String userCorrection;

    @Column(name = "name_on_map", columnDefinition = "text")
    private String nameOnMap;

    private String mapType;

    private float lat;

    private float lon;

    private boolean processed;

    public ProperName(){};

    public ProperName(String orth, String base, String type) {
        this.orth = orth;
        this.base = base;
        this.type = type;
    }
}
