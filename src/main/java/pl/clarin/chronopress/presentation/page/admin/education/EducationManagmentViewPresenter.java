package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.education.boundary.PageFacade;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
@Slf4j
public class EducationManagmentViewPresenter extends AbstractPresenter<EducationManagmenView> {

    @Inject
    PageFacade facade;
    
    @Override
    protected void onViewEnter() {
        getView().setPages(facade.findAll());
    }

    public void onRemovePage(@Observes(notifyObserver = Reception.IF_EXISTS) RemoveEducationPageEvent event) {
        if(event.getPage() != null) {
            getView().removePage(event.getPage());
            facade.removePage(event.getPage());
            Notification.show("Strona usunięta", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    public void onSavePage(@Observes(notifyObserver = Reception.IF_EXISTS) SaveEducationPageEvent event) {

        try {
            EducationPage page = event.getPage();
            facade.savePage(page);

            getView().removePage(event.getPage());
            getView().addPage(page);
            Notification.show("Strona zapisana", Notification.Type.TRAY_NOTIFICATION);
        } catch (Exception ex) {
            Notification.show("Zpisanie strony  nie powiodło się", Notification.Type.TRAY_NOTIFICATION);
            log.debug("Unable to save page", ex);
        }

    }
    
    public void onShowPage(@Observes(notifyObserver = Reception.IF_EXISTS) ShowEducationPageEvent event) {
        getView().showPage(event.getPage());
    }
}
