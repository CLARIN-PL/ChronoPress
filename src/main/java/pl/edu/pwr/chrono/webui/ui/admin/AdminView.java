package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
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
    private final VerticalLayout content = new VerticalLayout();
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

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        Accordion accordion = new Accordion();
        accordion.addTab(buildDataAnalysis());
        accordion.addTab(buildApplicationSettings());
        accordion.addTab(buildEducationSettings());

        layout.addComponent(accordion);
        layout.addComponent(content);

        addComponent(layout);
    }

    private VerticalLayout buildDataAnalysis() {
        VerticalLayout v = new VerticalLayout();
        v.setCaption("Ustawienia analizy");
        v.addComponent(new Button("Pole leksykalne"));
        v.addComponent(new Button("Odbiorcy"));
        return v;
    }

    private VerticalLayout buildApplicationSettings() {
        VerticalLayout v = new VerticalLayout();
        v.setCaption("Ustawienia aplikacji");
        v.addComponent(new Button("Opisy"));
        return v;
    }

    private VerticalLayout buildEducationSettings() {
        VerticalLayout v = new VerticalLayout();
        v.setCaption("Edukacja");
        v.addComponent(new Button("Główne strony"));
        v.addComponent(new Button("Pod strony"));
        return v;
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}