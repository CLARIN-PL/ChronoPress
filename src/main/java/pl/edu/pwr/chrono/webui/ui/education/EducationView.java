package pl.edu.pwr.chrono.webui.ui.education;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.FilterPanel;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

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
        List<String> tabs = Arrays.asList(
                "O projekcie", "Informacja o czasopismach", "Słownik terminów",
                "Podręcznik", "Baza publikacji");
        tabs.forEach(s -> sheet.addComponent(tab(s)));
    }

    public VerticalLayout tab(String title) {
        VerticalLayout l = new VerticalLayout();
        l.setSizeFull();
        l.setCaption(title);
        l.addComponent(content());
        return l;
    }

    public HorizontalLayout content() {
        HorizontalLayout hz = new HorizontalLayout();
        hz.setMargin(new MarginInfo(true, true, true, false));
        hz.setWidth(100, Unit.PERCENTAGE);

        FilterPanel panel = new FilterPanel();
        CssLayout content = new CssLayout();

        hz.addComponent(panel);
        hz.addComponent(content);

        hz.setExpandRatio(panel, 1);
        hz.setExpandRatio(content, 4);

        return hz;
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}