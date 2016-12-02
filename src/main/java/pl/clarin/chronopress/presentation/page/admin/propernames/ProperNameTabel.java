/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.propernames;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class ProperNameTabel extends CustomComponent{
    
    private MTable<ProperName> names;
    
    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ShowProperNameEvent> showPoperName;
    
    @PostConstruct
    public void inti(){
        names = new MTable<>(ProperName.class)
                .withStyleName(ValoTheme.TABLE_COMPACT, ValoTheme.TABLE_SMALL,ValoTheme.TABLE_BORDERLESS, ChronoTheme.TABLE)
                .withFullHeight()
                .withFullWidth()
                .withProperties("orth", "base", "userCorrection", "nameOnMap" ,"lat","lon")
                .withColumnHeaders(
                        provider.getProperty("label.orth"),
                        provider.getProperty("label.base"),
                        provider.getProperty("label.user.correction"),
                        provider.getProperty("label.name.on.map"),
                        provider.getProperty("label.lat"),
                        provider.getProperty("label.lon"));       

        names.setSelectable(true);
        names.addRowClickListener(event -> {
            showPoperName.fire(new ShowProperNameEvent((ProperName) event.getEntity()));
        });
        setCompositionRoot(names);
    }
    
    public ProperName getSelectedProperName(){
        return names.getValue();
    }
    
    public void setProperNames(List<ProperName> list) {
        names.removeAllItems();
        names.addBeans(list);
    }     
}
