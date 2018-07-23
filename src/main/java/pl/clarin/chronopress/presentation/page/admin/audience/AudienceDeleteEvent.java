/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.audience;

import lombok.Getter;
import pl.clarin.chronopress.business.audience.entity.Audience;

class AudienceDeleteEvent {
    
    @Getter
    private final Audience audience;

    public AudienceDeleteEvent(Audience audience) {
        this.audience = audience;
    }
}
