package pl.clarin.chronopress.presentation.page.samplebrowser;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import java.util.Date;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class SampleBrowserViewPresenter extends AbstractPresenter<SampleBrowserView> {

    @Inject
    SampleFacade facade;

    @Override
    protected void onViewEnter() {
    }

    public long size(String journal, Date publication, String article, String author) {
        long i = facade.findSamplesCount(journal, publication, article, author);
        return i;
    }

    public List<Sample> findEntities(int firstRow, boolean asc, String sortProperty, String journal, Date publication, String article, String author) {
        List<Sample> ss = facade.findSamples(firstRow, asc, sortProperty, journal, publication, article, author);
        return ss;
    }
    
     public void onShowSampleEvent(@Observes(notifyObserver = Reception.IF_EXISTS) ShowSampleEvent event) {
         getView().showSampleWindow(event.getSample());
    }
     
     public void onSaveSampleEvent(@Observes(notifyObserver = Reception.IF_EXISTS) SaveSampleEvent event) {
        try{
            Sample sample = event.getSample();
            sample = facade.save(sample);       
            Notification.show("Zapisanie powiodło się", Notification.Type.TRAY_NOTIFICATION);
        }catch (Exception ex){
            Notification.show("Zapisanie nie powiodło się!", Notification.Type.TRAY_NOTIFICATION);
            ex.printStackTrace();
        }
    } 

}
