package pl.clarin.chronopress.presentation.page.admin.propernames;

import com.vaadin.cdi.CDIView;
import com.vaadin.ui.UI;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(ProperNameView.ID)
@RolesAllowed({"moderator"})
public class ProperNameViewImpl extends AbstractView<ProperNameViewPresenter> implements ProperNameView {

    @Inject
    Instance<ProperNameViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    ProperNameTabel table;
    
    @Inject
    ProperNameWindow window;

    @PostConstruct
    public void init() {

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.propername.title")), table);

        setCompositionRoot(layout);
    }

    @Override
    protected ProperNameViewPresenter generatePresenter() {
        return presenter.get();
    }

    @Override
    public void setProperNames(List<ProperName> all) {
        table.setProperNames(all);
    }
    
    @Override
    public void showProperNameWindow(ProperName name){
        window.setProperName(name);
        UI.getCurrent().addWindow(window);
    }
}
