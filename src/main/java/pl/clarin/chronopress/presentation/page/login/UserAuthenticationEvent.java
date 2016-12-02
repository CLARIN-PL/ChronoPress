/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.login;

public class UserAuthenticationEvent {

    private final String username;
    private final String password;

    public UserAuthenticationEvent(final String user, final String password) {
        this.username = user;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

    