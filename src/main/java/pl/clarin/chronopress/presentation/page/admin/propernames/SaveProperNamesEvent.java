/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.propernames;

import lombok.Getter;
import pl.clarin.chronopress.business.propername.entity.ProperName;

public class SaveProperNamesEvent {

    @Getter
    private ProperName properName;

    public SaveProperNamesEvent(ProperName properName) {
        this.properName = properName;
    }
}
