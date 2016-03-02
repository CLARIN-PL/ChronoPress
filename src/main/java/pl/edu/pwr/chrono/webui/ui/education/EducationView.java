package pl.edu.pwr.chrono.webui.ui.education;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = EducationView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class EducationView extends DefaultView<EducationPresenter> implements View {

    public static final String VIEW_NAME = "education";
    private final TabSheet sheet = new TabSheet();
    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    public EducationView(EducationPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(FontAwesome.BOOK, provider.getProperty("view.education.title")));
        setSpacing(true);
        sheet.setSizeFull();
        sheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        addComponent(sheet);
        initTabs();
    }

    public void initTabs() {
        presenter.getPageAggregators().forEach(p -> {
            sheet.addComponent(new PageTab(p, provider));
        });
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}