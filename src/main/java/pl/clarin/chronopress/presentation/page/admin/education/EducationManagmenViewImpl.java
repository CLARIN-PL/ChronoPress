package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(EducationManagmenView.ID)
@RolesAllowed({"moderator"})
public class EducationManagmenViewImpl extends AbstractView<EducationManagmentViewPresenter> implements EducationManagmenView {

    @Inject
    private Instance<EducationManagmentViewPresenter> presenterInstance;
    
    @Inject
    DbPropertiesProvider provider;
        
    @Inject
    EducationPageTable pageTable;
    
    @Inject
    EducationPageWindow pageWindow;
    
    @Inject
    javax.enterprise.event.Event<RemoveEducationPageEvent> removeEducationPageEvent;
    
    @PostConstruct
    public void init() {

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.education.title")),
                       buildButtons(), pageTable);
        setCompositionRoot(layout);
    }

    private MHorizontalLayout buildButtons() {

        MButton createPage = new MButton(provider.getProperty("button.create.page"))
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    showPage(new EducationPage());
                });
        
         MButton removePage = new MButton(provider.getProperty("button.remove.page"))
                .withIcon(FontAwesome.TRASH)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    EducationPage page = pageTable.getSelectedPage();
                    if(page != null){
                       removeEducationPageEvent.fire(new RemoveEducationPageEvent(page));
                    }
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(createPage, removePage);

    }

    @Override
    public void showPage(EducationPage page) {
         pageWindow.setPage(page);
         UI.getCurrent().addWindow(pageWindow);
    }

    @Override
    protected EducationManagmentViewPresenter generatePresenter() {
       return presenterInstance.get();
    }  

    public void setPages(List<EducationPage> pages){
        pageTable.setPages(pages);
    }

    @Override
    public void removePage(EducationPage page) {
        pageTable.removePage(page);
    }

    @Override
    public void addPage(EducationPage page) {
        pageTable.addPage(page);
    }
}
