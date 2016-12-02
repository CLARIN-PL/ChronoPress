package pl.clarin.chronopress.presentation.page.admin.labels;

import com.vaadin.cdi.UIScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.property.boundary.PropertyFacade;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class LabelsViewPresenter extends AbstractPresenter<LabelsView> {
    
    @Inject
    PropertyFacade repository;
    
    @Override
    protected void onViewEnter() {
        getView().setProperties(repository.findAll());
    }
    
    public void onSaveProperty(@Observes(notifyObserver = Reception.IF_EXISTS) SavePropertyEvent event){
        repository.save(event.getProperty());
    }

}
