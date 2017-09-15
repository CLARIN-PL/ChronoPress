package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.server.Responsive;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class MainLayout extends VerticalLayout {

    private final VerticalLayout content = new VerticalLayout();

    @Inject
    Header header;

    @Inject
    Footer footer;

    @PostConstruct
    public void init() {
        Responsive.makeResponsive(this);
        addStyleName(ChronoTheme.START_PANEL);
        addComponent(header);
        addComponent(content);
        addComponent(footer);
        content.setSizeFull();
        setExpandRatio(content, 1);
    }

    public VerticalLayout getContent() {
        return content;
    }

}
