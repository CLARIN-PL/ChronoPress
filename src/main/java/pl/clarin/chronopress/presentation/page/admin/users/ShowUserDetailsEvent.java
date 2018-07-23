/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

import lombok.Getter;
import pl.clarin.chronopress.business.user.entity.User;

class ShowUserDetailsEvent {
    
    @Getter
    private final User user;

    public ShowUserDetailsEvent(User user) {
        this.user = user;
    }
 
}
