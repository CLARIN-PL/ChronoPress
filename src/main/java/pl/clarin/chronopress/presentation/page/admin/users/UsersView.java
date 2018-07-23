/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

import java.util.List;
import pl.clarin.chronopress.business.user.entity.User;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

public interface UsersView extends ApplicationView<UsersViewPresenter> {

    public static final String ID = "users";

    void showUser(User user);

    public void addUser(User user);

    public void removeUser(User user);

    public void setUsers(List<User> allUsers);
}
