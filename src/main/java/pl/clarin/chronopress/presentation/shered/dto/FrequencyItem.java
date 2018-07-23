/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.shered.dto;

import lombok.Data;

@Data
public class FrequencyItem {

    private String word;
    private Long value;
    private String partOfSpeech;
    private double percentage = 0;

    public FrequencyItem(String word, Long count) {
        this.word = word;
        this.value = count;
    }

    public FrequencyItem(String word, String partOfSpeech, Long count) {
        this.word = word;
        this.value = count;
        this.partOfSpeech = partOfSpeech;
    }

}
