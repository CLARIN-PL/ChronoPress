package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

@Data
public class LexemeProfile {

    private String baseColocat;
    private String match;
    private long count;
    private float percentage;

    public LexemeProfile(String baseColocat, String match ){
        this.baseColocat = baseColocat;
        this.match = match;
    }
}
