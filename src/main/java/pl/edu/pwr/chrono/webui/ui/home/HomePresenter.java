package pl.edu.pwr.chrono.webui.ui.home;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

/**
 * Created by tnaskret on 10.01.16.
 */
@SpringComponent
@UIScope
public class HomePresenter extends Presenter<HomeView> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HomePresenter.class);

    public void showInstitutionPersonList() {
    }
}
