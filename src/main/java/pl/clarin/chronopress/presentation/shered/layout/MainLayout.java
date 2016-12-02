package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.server.Responsive;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class MainLayout extends VerticalLayout {

    private final VerticalLayout content = new VerticalLayout();

    @Inject
    Header header;

    @Inject
    Footer footer;

    @PostConstruct
    public void init() {
        Responsive.makeResponsive(this);
        addComponent(header);
        addComponent(content);
        addComponent(footer);
    }

    public VerticalLayout getContent() {
        return content;
    }

}
