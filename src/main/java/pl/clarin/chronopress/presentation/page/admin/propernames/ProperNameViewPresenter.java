package pl.clarin.chronopress.presentation.page.admin.propernames;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.propername.boundary.ProperNameFacade;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@Slf4j
@UIScoped
public class ProperNameViewPresenter extends AbstractPresenter<ProperNameView> {
    
    @Inject
    ProperNameFacade facade;
    
    @Override
    protected void onViewEnter() {
        getView().setProperNames(facade.findAllGeolocation());
    }
    
    public void onSaveProperName(@Observes(notifyObserver = Reception.IF_EXISTS) SaveProperNamesEvent event){
         try {
            facade.save(event.getProperName());
            Notification.show("Nazwa własna zapisana", Notification.Type.TRAY_NOTIFICATION);
        } catch (Exception ex) {
            Notification.show("Zpisanie nie powiodło się", Notification.Type.TRAY_NOTIFICATION);
            log.debug("Unable to save page", ex);
        }
    }
    
    public void onShowProperName(@Observes(notifyObserver = Reception.IF_EXISTS) ShowProperNameEvent event){
        getView().showProperNameWindow(event.getProperName());
    }

}
