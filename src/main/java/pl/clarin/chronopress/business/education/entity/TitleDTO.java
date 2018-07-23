/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.education.entity;

import lombok.Getter;


public class TitleDTO {

    @Getter
    private final Long id;

    @Getter
    private final String category;
    
    @Getter
    private final String title;

    public TitleDTO(Long id, String category, String title) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

}
