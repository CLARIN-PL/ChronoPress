package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 05.02.16.
 */
@SpringComponent
@ViewScope
public class DataSelectionTab extends VerticalLayout{

    private final Button acceptButton = new Button("Akceptuj");
    private final Button clearButton  = new Button("Wyczyść");

    @PostConstruct
    public void init(){
        setMargin(true);
        setCaption("Selekcja danych");

        addComponent(initializeSearchPanel());
        addComponent(buildButtonBar());
    }

    private HorizontalLayout initializeSearchPanel(){
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();
        layout.addComponent(left);
        layout.addComponent(right);
        return layout;
    }

    public HorizontalLayout buildButtonBar(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        layout.addComponent(acceptButton);
        layout.addComponent(clearButton);

        return layout;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
}
