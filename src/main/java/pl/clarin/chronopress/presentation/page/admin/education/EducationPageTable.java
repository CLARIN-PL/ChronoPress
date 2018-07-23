/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;


public class EducationPageTable extends CustomComponent{
    
    private MTable<EducationPage> pages;
    
    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ShowEducationPageEvent> showDetails;
    
    @PostConstruct
    public void inti(){
        pages = new MTable<>(EducationPage.class)
                .withStyleName(ValoTheme.TABLE_COMPACT, ValoTheme.TABLE_SMALL,ValoTheme.TABLE_BORDERLESS, ChronoTheme.TABLE)
                .withFullHeight()
                .withFullWidth()
                .withProperties("category", "pageTitle", "published")
                .withColumnHeaders(
                        provider.getProperty("label.category"),
                        provider.getProperty("label.page.title"),
                        provider.getProperty("label.published"));       

        pages.setSelectable(true);
        pages.addRowClickListener(event -> {
            showDetails.fire(new ShowEducationPageEvent((EducationPage) event.getEntity()));
        });
        setCompositionRoot(pages);
    }
  
    public EducationPage getSelectedPage(){
        return pages.getValue();
    }
    
    public void setPages(List<EducationPage> list) {
        pages.removeAllItems();
        pages.addBeans(list);
    }
    
    public void addPage(EducationPage u){
        pages.addBeans(u);
    }

    void removePage(EducationPage u) {
       pages.removeItem(u);
    }
}
