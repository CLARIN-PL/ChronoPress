package pl.edu.pwr.chrono.webui.ui.admin.data;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.infrastructure.components.UploadBox;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name =  DataManagementView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class DataManagementView extends DefaultView<DataManagementPresenter> implements View {

    public static final String VIEW_NAME = "data-management";

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    private UploadBox uploadProperNames;

    @Value("${upload}")
    private String uploadPath;

    @Autowired
    public  DataManagementView(DataManagementPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.data.management.title")));
        setSpacing(true);
        addComponent(buildProperNamesOperation());
        addComponent(buildGeolocationsOperation());
    }

    public HorizontalLayout buildProperNamesOperation(){
        HorizontalLayout l = new HorizontalLayout();
        l.setSpacing(true);
        l.addComponent(new Label(provider.getProperty("view.data.management.load.proper.names")));

        uploadProperNames.setDestination(uploadPath);
        uploadProperNames.setOperationAfterUpload(filename -> {
            presenter.processUploadingProperNames(filename);
        });
        l.addComponent(uploadProperNames);

        return l;
    }

    public HorizontalLayout buildGeolocationsOperation(){
        HorizontalLayout l = new HorizontalLayout();
        l.setSpacing(true);
        l.addComponent(new Label(provider.getProperty("view.data.management.load.geolocation")));

        Button execute = new Button(provider.getProperty("view.data.management.load.geolocation.button"));
        execute.addClickListener(e -> {
                    execute.setEnabled(false);
                    presenter.processGeolocations();
                    execute.setEnabled(true);
                }
        );
        l.addComponent(execute);

        return l;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
