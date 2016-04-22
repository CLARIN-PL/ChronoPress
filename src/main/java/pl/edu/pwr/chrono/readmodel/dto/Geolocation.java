package pl.edu.pwr.chrono.readmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Geolocation {

    private String place_id;
    private String licence;
    private String osm_type;
    private String osm_id;
    private float[] boundingbox;
    private float lat;
    private float lon;
    private String display_name;

    @JsonProperty("class")
    private String klass;

    private String type;
    private float importance;
    private String icon;

}
