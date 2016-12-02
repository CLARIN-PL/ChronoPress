package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class SimpleGeolocation {

    private float lat;
    private float lon;
    private String display_name;
    private String base;
    private long freq;

    public SimpleGeolocation(String name, String base, float lat, float lon){
        this.display_name = name;
        this.base = base;
        this.lat = lat;
        this.lon = lon;
    }
}
