package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

@Data
public class SimpleGeolocation {

    private float lat;
    private float lon;
    private String display_name;
    private long freq;

    public SimpleGeolocation(String name, float lat, float lon){
        this.display_name = name;
        this.lat = lat;
        this.lon = lon;
    }
}
