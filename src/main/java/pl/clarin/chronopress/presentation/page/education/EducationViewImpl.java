package pl.clarin.chronopress.presentation.page.education;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.TitleDTO;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(EducationView.ID)
public class EducationViewImpl extends AbstractView<EducationViewPresenter> implements EducationView {

    @Inject
    Instance<EducationViewPresenter> presenterInstance;
    
    @Inject
    DbPropertiesProvider provider;

    private final TabSheet sheet = new TabSheet();

    @Inject
    Instance<CategoryTab> tabs;
    
    @PostConstruct
    public void init() {
        sheet.setSizeFull();
        sheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        
        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(new MMarginInfo(false, true))
                .withFullHeight()
                .withFullWidth()
                .with(new Title(FontAwesome.GRADUATION_CAP, provider.getProperty("view.education.title")), sheet);
        setCompositionRoot(layout);
    }

    @Override
    protected EducationViewPresenter generatePresenter() {
       return presenterInstance.get();
    }
    
    @Override
    public void addTab(String catgeory, List<TitleDTO> list){
        CategoryTab tab = tabs.get();
        tab.setCategory(catgeory);
        tab.setTitles(list);
        sheet.addComponent(tab);
    }
}
