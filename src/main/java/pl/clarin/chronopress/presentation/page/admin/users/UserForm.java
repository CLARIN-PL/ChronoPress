/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.user.entity.User;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class UserForm extends CustomComponent {

    @PropertyId("username")
    private final MTextField username = new MTextField();

    @PropertyId("email")
    private final MTextField email = new MTextField();

    @PropertyId("role")
    private final ComboBox role = new ComboBox("Rola", Arrays.asList(User.Role.values()));

    private final BeanFieldGroup<User> binder = new BeanFieldGroup<>(User.class);

    @Inject
    DbPropertiesProvider provider;
        
    @PostConstruct
    public void init() {
        binder.bindMemberFields(this);
        username.setCaption(provider.getProperty("label.username"));
        username.setNullRepresentation("");

        email.setCaption(provider.getProperty("label.email"));
        email.setNullRepresentation("");

        role.setNullSelectionAllowed(false);
        
         MFormLayout form = new MFormLayout()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT)
                .withFullWidth();

        form.addComponents(username, email, role);
        
        setCompositionRoot(form);
    }

    public User getUser() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    public void setUser(User user) {
        binder.setItemDataSource(user);
    }
}
