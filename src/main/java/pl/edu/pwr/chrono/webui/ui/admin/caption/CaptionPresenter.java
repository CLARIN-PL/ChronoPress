package pl.edu.pwr.chrono.webui.ui.admin.caption;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Property;
import pl.edu.pwr.chrono.repository.PropertyRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

/**
 * Created by tnaskret on 04.01.16.
 */

@SpringComponent
@UIScope
@Slf4j
public class CaptionPresenter extends Presenter<CaptionsView> {

    @Autowired
    private DbPropertiesProvider propertiesProvider;

    @Autowired
    private PropertyRepository repository;

    public void refreshAction() {
        propertiesProvider.loadProperties();
    }

    public void saveProperty(Property property) {
        repository.save(property);
    }

    public void loadPropertyList() {
        view.getGridSection().showLoading();
        view.loadPropertyList(repository.findAll());
    }
}
