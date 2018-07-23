package pl.clarin.chronopress.presentation.page.admin.audience;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.audience.boundary.AudienceFacade;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class AudienceViewPresenter extends AbstractPresenter<AudienceView> {

    @Inject
    private AudienceFacade facade;

    public void onAudienceSaveEvent(@Observes(notifyObserver = Reception.IF_EXISTS) AudienceSaveEvent event) {
        try{
            Audience audience = event.getAudience();
            audience = facade.save(audience);       
        if(!event.getAudience().isPersistent()){
            getView().swapAudience(audience, audience);
        }else {
            getView().swapAudience(event.getAudience(), audience);
        }
            Notification.show("Zapisanie powiodło się", Notification.Type.TRAY_NOTIFICATION);
        }catch (Exception ex){
            Notification.show("Zapisanie nie powiodło się!", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    public void onAudienceDeleteEvent(@Observes(notifyObserver = Reception.IF_EXISTS) AudienceDeleteEvent event) {
        facade.remove(event.getAudience());
        Notification.show("Usunięcie powiodło się", Notification.Type.TRAY_NOTIFICATION);
    }
    
    public List<String> getJournalTitles(){
        return facade.findJournalNames();
    }
    
    @Override
    protected void onViewEnter() {
        getView().setAudience(facade.findAll());
    }
    
}
