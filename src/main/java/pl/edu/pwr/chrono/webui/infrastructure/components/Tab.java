package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 10.02.16.
 */


public abstract class Tab  extends VerticalLayout{

    private final Button acceptButton = new Button();
    private final Button clearButton = new Button();


    protected HorizontalLayout buttonBar;
    protected VerticalLayout loadingIndicator;
    protected final VerticalLayout mainPanelContent = new VerticalLayout();

    @Autowired
    protected DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);

        setCaption(provider.getProperty("view.tab.data.selection.title"));

        loadingIndicator = initializeLoading();
        buttonBar = initializeButtonBar();

        initializeTab();
    }

    public abstract void initializeTab();

    public HorizontalLayout initializeButtonBar(){

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeUndefined();
        layout.setSpacing(true);

        acceptButton.setCaption(provider.getProperty("button.accept"));
        clearButton.setCaption(provider.getProperty("button.clear"));
        acceptButton.setIcon(FontAwesome.CHECK);
        acceptButton.addStyleName(ValoTheme.BUTTON_SMALL);
        acceptButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        clearButton.addStyleName(ValoTheme.BUTTON_SMALL);

        layout.addComponent(clearButton);
        layout.addComponent(acceptButton);

        return layout;
    }

    public VerticalLayout initializeLoading(){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);

        HorizontalLayout loadingNotification = new HorizontalLayout();
        loadingNotification.setSpacing(true);
        loadingNotification.addComponents(progressBar, new Label(provider.getProperty("label.loading")));

        layout.addComponents(loadingNotification);
        layout.setComponentAlignment(loadingNotification, Alignment.MIDDLE_CENTER);

        return layout;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
}
