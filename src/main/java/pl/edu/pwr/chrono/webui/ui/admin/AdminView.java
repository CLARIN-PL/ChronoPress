package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.admin.caption.CaptionsView;
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
    private DbPropertiesProvider properties;

    @Autowired
    public AdminView(AdminPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    void init() {
        setMargin(new MarginInfo(false, true, true, true));

        addComponent(new Title(properties.getProperty("view.admin.panel.title")));

        addComponent(systemSection());

    }

    public VerticalLayout systemSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addStyleName(ChronoTheme.ADMIN_PANEL);

        Button captionsManagement = addButton(properties.getProperty("view.admin.panel.caption.button"));
        captionsManagement.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(CaptionsView.VIEW_NAME));

        Button lexicalFiledManagement = addButton(properties.getProperty("view.admin.panel.lexical.field.button"));

        Button audienceManagement = addButton(properties.getProperty("view.admin.panel.audience.button"));

        Button educationManagement = addButton(properties.getProperty("view.admin.panel.education.button"));

        layout.addComponent(row(ChronoTheme.ICON_TAG, captionsManagement, provider.getProperty("view.admin.panel.caption.desc")));
        layout.addComponent(row(ChronoTheme.ICON_HELMET, lexicalFiledManagement, provider.getProperty("view.admin.panel.lexical.field.desc")));
        layout.addComponent(row(ChronoTheme.ICON_USERS, audienceManagement, provider.getProperty("view.admin.panel.audience.button.desc")));
        layout.addComponent(row(ChronoTheme.ICON_LOG, educationManagement, provider.getProperty("view.admin.panel.education.button.desc")));

        return layout;
    }

    private HorizontalLayout row(String firstIcon, Button firstButton, String firstDesc) {
        float height = 25f;
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth(100, Unit.PERCENTAGE);
        row.addStyleName(ChronoTheme.ADMIN_PANEL_ROW);
        row.setHeight(height, Unit.PIXELS);

        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName(ChronoTheme.ADMIN_PANEL_LEFT_COLUMN);
        layout.setSizeFull();

        HorizontalLayout column1 = columnButtonWithIcon(firstButton, addIcon(firstIcon));
        VerticalLayout column2 = addDescription(firstDesc);

        row.addComponent(column1);
        row.setExpandRatio(column1, 1.0f);
        row.addComponent(column2);
        row.setExpandRatio(column2, 1.5f);
        return row;
    }

    private HorizontalLayout columnButtonWithIcon(Button button, HorizontalLayout icon) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName(ChronoTheme.ADMIN_PANEL_LEFT_COLUMN);

        layout.setSizeFull();
        layout.addComponent(icon);
        layout.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
        layout.addComponent(button);
        layout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(button, 1.0f);
        return layout;
    }

    private HorizontalLayout addIcon(String style) {
        HorizontalLayout icon = new HorizontalLayout();
        icon.setWidth("18px");
        icon.setHeight("18px");
        icon.addStyleName(style);
        return icon;

    }

    private Button addButton(String name) {
        Button button = new Button(name);
        button.addStyleName(ValoTheme.BUTTON_LINK);
        button.addStyleName(ValoTheme.BUTTON_TINY);
        button.addStyleName(ChronoTheme.BUTTON_LINK);
        return button;
    }

    private VerticalLayout addDescription(String description) {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName(ChronoTheme.ADMIN_PANEL_RIGHT_COLUMN);
        layout.setSizeFull();
        Label desc = new Label(description);
        layout.addComponent(desc);
        layout.setComponentAlignment(desc, Alignment.MIDDLE_CENTER);
        return layout;
    }


    @Override
    public void enter(ViewChangeEvent event) {
    }
}