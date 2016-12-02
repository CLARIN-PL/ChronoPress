/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class LexicalFieldForm extends CustomComponent {

    @PropertyId("groupName")
    private final TextField group = new TextField();
    private final BeanFieldGroup<LexicalField> binder = new BeanFieldGroup<>(LexicalField.class);

    @Inject
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        binder.bindMemberFields(this);

        group.setCaption(provider.getProperty("label.lexical.group.name"));
        group.setNullRepresentation("");

        FormLayout form = new MFormLayout()
                .withFullWidth()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT);
        form.addComponent(group);

        VerticalLayout layout = new MVerticalLayout()
                .withMargin(true)
                .withSpacing(true)
                .with(form);
        
        setCompositionRoot(layout);
    }

    public LexicalField getItem() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
        return binder.getItemDataSource().getBean();
    }

    public void setItem(LexicalField lexicalField) {
        binder.setItemDataSource(lexicalField);
    }

    public void cancel(){
        binder.discard();
    }
}
