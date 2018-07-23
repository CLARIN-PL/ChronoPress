/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.audience;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class AudienceForm  extends  CustomComponent{
    
    private final BeanFieldGroup<Audience> binder = new BeanFieldGroup<>(Audience.class);

    @PropertyId("audienceName")
    private final TextField group = new MTextField();

    private TwinColSelect selectJournal;
        
    @Inject
    DbPropertiesProvider provider;
    
    @PostConstruct
    public void init(){
        binder.bindMemberFields(this);
        
        group.setCaption(provider.getProperty("label.audience.group.name"));
        group.setNullRepresentation("");
        
        selectJournal = new TwinColSelect();
        selectJournal.setWidth(100, Sizeable.Unit.PERCENTAGE);
        selectJournal.setNullSelectionAllowed(true);
        selectJournal.setMultiSelect(true);
        selectJournal.setImmediate(true);
        selectJournal.setLeftColumnCaption(provider.getProperty("select.audience.available"));
        selectJournal.setRightColumnCaption(provider.getProperty("select.audience.selected"));
        selectJournal.addValueChangeListener(e -> 
                binder.getItemDataSource().getBean()
                .setJournalTitle((Set<String>) e.getProperty().getValue()));
        
        MFormLayout form = new MFormLayout()
                .withFullWidth()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT);
        
        form.addComponent(group);
        form.addComponent(selectJournal);
        
        setCompositionRoot(form);
    }
    
    public void cleanUp(){
        binder.discard();
        selectJournal.removeAllItems();
    }
    
    public void setReferenceNames(List<String> list){
            selectJournal.addItems(list);
    }

    public void setItem(Audience p) {
        binder.setItemDataSource(p);
        selectJournal.setValue(p.getJournalTitle());
    }

     public Audience getItem() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
        return binder.getItemDataSource().getBean();
    }
}
