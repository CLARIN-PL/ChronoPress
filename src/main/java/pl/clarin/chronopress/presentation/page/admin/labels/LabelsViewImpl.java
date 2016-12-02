package pl.clarin.chronopress.presentation.page.admin.labels;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.property.entity.Property;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.layout.FilterBar;

@CDIView(LabelsView.ID)
@RolesAllowed({ "moderator"})
public class LabelsViewImpl extends AbstractView<LabelsViewPresenter> implements LabelsView {

    @Inject
    Instance<LabelsViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;
    
    @Inject
    FilterBar filter;
    
    @Inject
    LabelsTabel table;
    
    private MButton refresh;
    
    @PostConstruct
    public void init() {
        
        refresh = new MButton(provider.getProperty("view.captions.list.refresh.button"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.REFRESH)
                .withListener(e -> {
                    provider.loadProperties();
                    Notification.show("Opisy pól zostały przeładowane", Notification.Type.TRAY_NOTIFICATION);
                });
                
        
        filter.addButton(refresh);
        filter.addValueChangeListener( e -> table.addConteinerFilter(e.getProperty().getValue().toString()));
        filter.addTextChangeListener(e -> table.addConteinerFilter(e.getText()));
        
        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.caption.editor.title")), filter, table);
        
        setCompositionRoot(layout);
    }

    public void setProperties(List<Property> all){
        table.setProperties(all);
    }
    
    @Override
    protected  LabelsViewPresenter generatePresenter() {
        return presenter.get();   
    }

}
