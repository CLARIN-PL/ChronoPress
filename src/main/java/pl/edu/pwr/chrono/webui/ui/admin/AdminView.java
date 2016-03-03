package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MGridLayout;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.admin.caption.CaptionsView;
import pl.edu.pwr.chrono.webui.ui.admin.education.EducationEditorView;
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
    private DbPropertiesProvider properties;
    private MGridLayout layout = new MGridLayout();

    @Autowired
    public AdminView(AdminPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    void init() {
        setMargin(new MarginInfo(false, true, true, true));

        addComponent(new Title(properties.getProperty("view.admin.panel.title")));

        layout.setWidth(70, Unit.PERCENTAGE);
        layout.setColumns(3);
        layout.setRows(2);
        layout.setColumnExpandRatio(0, 1);
        layout.setColumnExpandRatio(1, 1);
        layout.setColumnExpandRatio(2, 1);


        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);
        wrapper.addComponent(layout);
        wrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        addComponent(wrapper);

        initButtons();
    }

    public void initButtons() {

        Button lexicalFiledManagement = addButton(properties.getProperty("view.admin.panel.lexical.field.button"), FontAwesome.LEAF);

        Button audienceManagement = addButton(properties.getProperty("view.admin.panel.audience.button"), FontAwesome.USERS);

        Button educationManagement = addButton(properties.getProperty("view.admin.panel.education.button"), FontAwesome.GRADUATION_CAP);
        educationManagement.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(EducationEditorView.VIEW_NAME));

        Button captionsManagement = addButton(properties.getProperty("view.admin.panel.caption.button"), FontAwesome.TAGS);
        captionsManagement.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(CaptionsView.VIEW_NAME));

        Button userManagement = addButton(properties.getProperty("view.admin.panel.user.button"), FontAwesome.USER);
        layout.addComponent(lexicalFiledManagement, 0, 0);
        layout.addComponent(audienceManagement, 1, 0);
        layout.addComponent(educationManagement, 2, 0);

        layout.addComponent(captionsManagement, 0, 1);
        layout.addComponent(userManagement, 1, 1);

    }

    private Button addButton(String name, FontAwesome icon) {
        Button button = new Button(name);
        button.setIcon(icon);
        button.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        button.setWidth(100, Unit.PERCENTAGE);
        return button;
    }


    @Override
    public void enter(ViewChangeEvent event) {
    }
}