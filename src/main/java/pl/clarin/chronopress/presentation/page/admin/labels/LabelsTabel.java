/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.labels;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.property.entity.Property;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class LabelsTabel extends CustomComponent{
    
    private MVerticalLayout layout;
    
    private MGrid<Property> table;
    private BeanContainer<Long, Property> container;
    
    @Inject
    javax.enterprise.event.Event<SavePropertyEvent> savePropertyEvent;
    
    @Inject
    DbPropertiesProvider provider;
    
    @PostConstruct
    public void init() {
        setSizeFull();
         
        container = new BeanContainer<>(Property.class);
        container.setBeanIdProperty("id");
        
        table = new MGrid<>();
        
        table.setSizeFull();
        table.setContainerDataSource(container);
        table.setSelectionMode(Grid.SelectionMode.SINGLE);
        table.addStyleName(ChronoTheme.GRID);
        table.setReadOnly(true);
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(15);
        table.setEditorSaveCaption(provider.getProperty("button.save"));
        table.setEditorCancelCaption(provider.getProperty("button.cancel"));
        table.setEditorEnabled(true);
        table.setColumnOrder("key", "value");
        table.getColumn("key").setHeaderCaption(provider.getProperty("table.headers.key"));
        table.getColumn("value").setHeaderCaption(provider.getProperty("table.headers.value"));
        table.getColumn("lang").setHeaderCaption(provider.getProperty("table.headers.language"));
        table.getColumn("id").setHidden(true);
        table.setEditorFieldGroup(new BeanFieldGroup<>(Property.class));
        table.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {

            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                savePropertyEvent.fire(new SavePropertyEvent(container.getItem(table.getEditedItemId()).getBean()));
            }
        });
        layout = new  MVerticalLayout()
                .withMargin(new MMarginInfo(false, true))
                .withFullWidth()
                .with(table);
        setCompositionRoot(layout);
    }

    public void setProperties(List<Property> list){
        container.removeAllItems();
        container.addAll(list);
    }

    public void addConteinerFilter(String txt){
        container.removeAllContainerFilters();
        container.addContainerFilter(new LabelsTextFilter(txt));
    }
        
}
