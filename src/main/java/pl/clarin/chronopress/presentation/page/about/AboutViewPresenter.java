package pl.clarin.chronopress.presentation.page.about;

import com.vaadin.cdi.UIScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.education.boundary.PageFacade;
import pl.clarin.chronopress.business.education.entity.HomePage;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class AboutViewPresenter extends AbstractPresenter<AboutView> {

    @Inject
    PageFacade facade;

    @Override
    protected void onViewEnter() {
        HomePage page = facade.getHomePage(VaadinUI.getLanguage());
        if (page != null) {
            getView().setContent(page.getContent());
        }
    }

    public void onSaveHomePage(@Observes(notifyObserver = Reception.IF_EXISTS) SaveAboutPageEvent event) {
        facade.saveHomePage(event.getPage());
    }

    public void onEditHomePage(@Observes(notifyObserver = Reception.IF_EXISTS) EditAboutPageEvent event) {
        getView().showEditHomePage(facade.getHomePage(VaadinUI.getLanguage()));
    }
}
