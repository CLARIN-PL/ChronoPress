/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.education;

import lombok.Getter;
import pl.clarin.chronopress.business.education.entity.EducationPage;


class RemoveEducationPageEvent {
    
    @Getter
    private final EducationPage page;

    public RemoveEducationPageEvent(EducationPage page) {
        this.page = page;
    }
}
