/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.user.entity.User;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class UserTable extends CustomComponent{
    
    private MTable<User> users;
    
    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ShowUserDetailsEvent> showUserDetails;
    
    @PostConstruct
    public void inti(){
        users = new MTable<>(User.class)
                .withStyleName(ValoTheme.TABLE_COMPACT, ValoTheme.TABLE_SMALL,ValoTheme.TABLE_BORDERLESS, ChronoTheme.TABLE)
                .withFullHeight()
                .withFullWidth()
                .withProperties("username", "email", "role")
                .withColumnHeaders(
                        provider.getProperty("label.username"),
                        provider.getProperty("label.email"),
                        provider.getProperty("label.role"));       

        users.setSelectable(true);
        users.addRowClickListener(event -> {
            showUserDetails.fire(new ShowUserDetailsEvent((User) event.getEntity()));
        });
        setCompositionRoot(users);
    }
  
    public User getSelectedUser(){
        return users.getValue();
    }
    
    public void setUsers(List<User> list) {
        users.removeAllItems();
        users.addBeans(list);
    }
    
    public void addUser(User u){
        users.addBeans(u);
    }

    void removeUser(User u) {
       users.removeItem(u);
    }
}
