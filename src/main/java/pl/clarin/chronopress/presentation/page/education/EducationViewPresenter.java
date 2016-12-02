package pl.clarin.chronopress.presentation.page.education;

import com.vaadin.cdi.UIScoped;
import javax.inject.Inject;
import pl.clarin.chronopress.business.education.boundary.PageFacade;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class EducationViewPresenter extends AbstractPresenter<EducationView> {

    @Inject            
    PageFacade facad;
    
    @Override
    protected void onViewEnter() {
        facad.getPagesCategorisWithTitles().forEach((k, v) -> {
            getView().addTab(k, v);
        });
    }
    
    public EducationPage getEducationPageById(Long id){
        return facad.getPageById(id);
    }
}
