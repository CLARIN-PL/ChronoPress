/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.shered.dto;

import java.util.List;
import lombok.Data;

@Data
public class InitDataSelectionDTO {
    
    List<String> journalTitles;
    
    List<Integer> Years;

    List<String> periods;

    List<Integer> expositions;

    List<String> authors;
    
    List<String> audience;

}
