package pl.clarin.chronopress.presentation.page.admin;

import com.vaadin.cdi.UIScoped;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.page.about.EditAboutPageEvent;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class AdminViewPresenter extends AbstractPresenter<AdminView> {
    
    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;
    
    @Inject
    javax.enterprise.event.Event<EditAboutPageEvent> editHomePage;
    
    @Override
    protected void onViewEnter() {
    }

    void navigateTo(String view) {
        navigation.fire(new NavigationEvent(view));
    }

    void showEditWindow() {
       editHomePage.fire(new EditAboutPageEvent());
    }
  
}
