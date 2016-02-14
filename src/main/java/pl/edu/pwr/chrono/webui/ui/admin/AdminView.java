package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = AdminView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class AdminView extends DefaultView<AdminPresenter> implements View {

    public static final String VIEW_NAME = "admin";

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    public AdminView(AdminPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(FontAwesome.BOOK, provider.getProperty("view.admin.title")));
        setSpacing(true);
        setWidth(100, Unit.PERCENTAGE);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}