/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import java.io.Serializable;
import lombok.Getter;


public class ShowConcordanceWindowEvent implements  Serializable{
    
    @Getter
    private String base;

    public ShowConcordanceWindowEvent(String base) {
        this.base = base;
    }
}
