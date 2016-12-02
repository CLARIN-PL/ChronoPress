package pl.clarin.chronopress.presentation.page.about;

import com.vaadin.cdi.CDIView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.HomePage;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(AboutView.ID)
public class AboutViewImpl extends AbstractView<AboutViewPresenter> implements AboutView {

    @Inject
    private Instance<AboutViewPresenter> presenterInstance;
    
    private Label content;
    
    @Inject
    AboutPageWindow window;
    
    @PostConstruct
    public void init() {
       
        content = new MLabel()
                .withContentMode(ContentMode.HTML)
                .withFullHeight()
                .withFullHeight();

        MVerticalLayout section = new MVerticalLayout()
                .withFullWidth()
                .with(content);
        
        MHorizontalLayout layout = new MHorizontalLayout()
                .withFullWidth()
                .withMargin(true)
                .withSpacing(true)
                .with(section)
                .withAlign(section, Alignment.MIDDLE_CENTER);
        
        setCompositionRoot(layout);
    }
    
    @Override
    protected AboutViewPresenter generatePresenter() {
       return presenterInstance.get();
    }
    
    public void setContent(String html){
        content.setValue(html);
    }
    
    public void showEditHomePage(HomePage page){
        window.setPage(page);
        UI.getCurrent().addWindow(window);
    }
}
