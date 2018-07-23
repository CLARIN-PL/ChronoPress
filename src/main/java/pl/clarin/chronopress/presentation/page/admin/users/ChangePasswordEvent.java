/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

public class ChangePasswordEvent {

    private final String username;
    
    public ChangePasswordEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
