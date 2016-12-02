/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.users;

import com.vaadin.data.Validator;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.layouts.MFormLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class PasswordForm extends CustomComponent{
    
    @Inject
    DbPropertiesProvider provider;
        
    private final MPasswordField password = new MPasswordField();
    private final MPasswordField confirmPassword = new MPasswordField();
    
    @PostConstruct
    public void init(){
        
        password.setCaption(provider.getProperty("label.password"));
        password.setNullRepresentation("");

        confirmPassword.setCaption(provider.getProperty("label.confirm.password"));
        confirmPassword.setNullRepresentation("");
        confirmPassword.setImmediate(true);
        confirmPassword.addValidator(value -> {
            if (!value.toString().equals(password.getValue())) {
                throw new Validator.InvalidValueException(provider.getProperty("validation.passwords.must.be.same"));
            }
        });

        MFormLayout form = new MFormLayout()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT)
                .withFullWidth();
        form.addComponents(password, confirmPassword);
        
        setCompositionRoot(form);
    }
    
    public void reset() {
        password.setValue("");
        confirmPassword.setValue("");
    }
    
   public String getPssword() {
        confirmPassword.validate();
        return confirmPassword.getValue();
    }

}
