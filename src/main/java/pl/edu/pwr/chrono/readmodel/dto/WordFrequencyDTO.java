package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

/**
 * Created by tnaskret on 18.02.16.
 */

@Data
public class WordFrequencyDTO {

    private String word;
    private String partOfSpeech;
    private long count = 0;
    private double percentage = 0;

    public WordFrequencyDTO(String word, long count) {
        this.word = word;
        this.count = count;
    }

    public WordFrequencyDTO(String word, String partOfSpeech, long count) {
        this.word = word;
        this.count = count;
        this.partOfSpeech = partOfSpeech;
    }

}
