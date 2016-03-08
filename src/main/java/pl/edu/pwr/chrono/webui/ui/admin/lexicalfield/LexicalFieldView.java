package pl.edu.pwr.chrono.webui.ui.admin.lexicalfield;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringView(name = LexicalFieldView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class LexicalFieldView extends DefaultView<LexicalFieldPresenter> implements View {

    public static final String VIEW_NAME = "lexical-field";

    @Autowired
    private DbPropertiesProvider provider;

    private MTable users;

    @Autowired
    public LexicalFieldView(LexicalFieldPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.lexical.field.title")));
        setSpacing(true);

        initButtons();

        HorizontalLayout buttons = buildButtons();
        addComponent(buttons);
    }

    private void initButtons() {
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button createAudience = new Button(provider.getProperty("button.create.lexical.group"));
        createAudience.addStyleName(ValoTheme.BUTTON_TINY);
        createAudience.addStyleName(ChronoTheme.BUTTON);
        createAudience.setIcon(FontAwesome.PLUS_CIRCLE);
        createAudience.addClickListener(event -> {
        });

        buttons.addComponent(createAudience);
        return buttons;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
