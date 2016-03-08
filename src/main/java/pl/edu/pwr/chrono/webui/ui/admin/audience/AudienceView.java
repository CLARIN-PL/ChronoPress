package pl.edu.pwr.chrono.webui.ui.admin.audience;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Audience;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.EntityComboBox;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = AudienceView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class AudienceView extends DefaultView<AudiencePresenter> implements View {

    public static final String VIEW_NAME = "audience";
    private final EntityComboBox<Audience> audience = new EntityComboBox("name", Audience.class);
    @Autowired
    private DbPropertiesProvider provider;
    @Autowired
    private AudienceWindow window;
    private Table names;

    @Autowired
    public AudienceView(AudiencePresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.audience.title")));
        setSpacing(true);

        initButtons();
        HorizontalLayout buttons = buildButtons();
        addComponent(buttons);

        initJournalTitlesTable();
        HorizontalLayout audienceSelection = buildAudienceSelection();

        addComponent(audienceSelection);
        addComponent(names);
    }

    private void initJournalTitlesTable() {
        names = new Table();
        names.addContainerProperty(provider.getProperty("label.journal.title"), String.class, null);
        names.addStyleName(ValoTheme.TABLE_COMPACT);
        names.addStyleName(ValoTheme.TABLE_SMALL);
        names.addStyleName(ValoTheme.TABLE_BORDERLESS);
        names.addStyleName(ChronoTheme.TABLE);
        names.setSizeFull();
    }

    public HorizontalLayout buildAudienceSelection() {

        Button edit = new Button(provider.getProperty("button.edit.audience.group"));
        edit.addStyleName(ValoTheme.BUTTON_TINY);
        edit.addStyleName(ChronoTheme.BUTTON);
        edit.setIcon(FontAwesome.EDIT);
        edit.addClickListener(e -> {
            window.setItem(audience.getContainer().getItem(audience.getValue()).getBean());
            UI.getCurrent().addWindow(window);
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.addComponent(new Label(provider.getProperty("label.audience.group")));
        layout.addComponent(audience);
        layout.addComponent(edit);

        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        audience.addValueChangeListener(event -> {
            loadNames();
        });
        return layout;
    }

    private void loadNames() {
        names.removeAllItems();
        audience.getContainer().getItem(audience.getValue()).getBean().getJournaltitle().forEach(i -> {
            names.addItem(new Object[]{i}, null);
        });
    }

    private void initButtons() {
        window.getCancel().addClickListener(event -> {
            UI.getCurrent().removeWindow(window);
        });
        window.getSave().addClickListener(event -> {
            presenter.save(window.getItem());
            loadAudience();
            loadNames();
            UI.getCurrent().removeWindow(window);
        });
    }

    private void loadAudience() {
        audience.getContainer().removeAllItems();
        audience.load(presenter.getAudience());
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button createAudience = new Button(provider.getProperty("button.create.audience.group"));
        createAudience.addStyleName(ValoTheme.BUTTON_TINY);
        createAudience.addStyleName(ChronoTheme.BUTTON);
        createAudience.setIcon(FontAwesome.PLUS_CIRCLE);
        createAudience.addClickListener(event -> {
            window.setItem(new Audience());
            UI.getCurrent().addWindow(window);
        });

        buttons.addComponent(createAudience);
        return buttons;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        loadAudience();
    }
}
