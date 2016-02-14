package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

public abstract class Tab  extends VerticalLayout{

    private final Button acceptButton = new Button();
    private final Button clearButton = new Button();

    @Autowired
    protected DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);

        initializeTab();
        initializeButtons();
    }

    public abstract void initializeTab();

    public void initializeButtons() {

        acceptButton.setCaption(provider.getProperty("button.accept"));
        acceptButton.setIcon(FontAwesome.CHECK);
        acceptButton.addStyleName(ValoTheme.BUTTON_SMALL);
        acceptButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        clearButton.setCaption(provider.getProperty("button.clear"));
        clearButton.addStyleName(ValoTheme.BUTTON_SMALL);
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
}
