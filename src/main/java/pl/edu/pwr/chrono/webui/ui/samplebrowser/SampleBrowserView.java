package pl.edu.pwr.chrono.webui.ui.samplebrowser;

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

@SpringView(name = SampleBrowserView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class SampleBrowserView extends DefaultView<SampleBrowserPresenter> implements View {

    public static final String VIEW_NAME = "sample-browser";

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    public SampleBrowserView(SampleBrowserPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(FontAwesome.BOOK, provider.getProperty("view.sample.browser.title")));
        setSpacing(true);
        setWidth(100, Unit.PERCENTAGE);

    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}