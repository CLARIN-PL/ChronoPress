/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.audience;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class NamesTable extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    private MTable<String> names;

    @PostConstruct
    public void init() {

        names = new MTable<String>()
                .withFullHeight()
                .withFullWidth()
                .withStyleName(ValoTheme.TABLE_COMPACT,
                        ValoTheme.TABLE_SMALL, ValoTheme.TABLE_BORDERLESS, ChronoTheme.TABLE);

        names.addContainerProperty(provider.getProperty("label.journal.title"), String.class, null);

        setCompositionRoot(names);
    }

    public void addBeans(Collection<String> list) {
        names.removeAllItems();
        list.forEach(i -> names.addItem(new Object[]{i}, null));
    }
    
    public void removeAll(){
        names.removeAllItems();
    }
}
