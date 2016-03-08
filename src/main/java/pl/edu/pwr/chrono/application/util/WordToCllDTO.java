package pl.edu.pwr.chrono.application.util;

import lombok.Data;

@Data
public class WordToCllDTO {

    private String filename;
    private String orth;
    private String base;
    private String ctag;
    private Integer sentenceId;

    public WordToCllDTO(String filename, String orth, String base, String ctag, Integer sentenceId) {
        this.filename = filename;
        this.orth = orth;
        this.base = base;
        this.ctag = ctag;
        this.sentenceId = sentenceId;
    }
}

