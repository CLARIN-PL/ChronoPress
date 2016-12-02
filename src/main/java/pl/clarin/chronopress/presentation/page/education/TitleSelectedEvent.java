/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.education;

import lombok.Getter;
import pl.clarin.chronopress.business.education.entity.TitleDTO;

public class TitleSelectedEvent {
    
    @Getter
    private final TitleDTO title;

    public TitleSelectedEvent(TitleDTO dto) {
        this.title = dto;
    }    
}
