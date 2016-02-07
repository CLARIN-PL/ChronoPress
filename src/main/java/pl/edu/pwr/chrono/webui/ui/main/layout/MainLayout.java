package pl.edu.pwr.chrono.webui.ui.main.layout;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.ui.main.layout.components.Footer;
import pl.edu.pwr.chrono.webui.ui.main.layout.components.Header;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 27.12.15.
 */

@SpringComponent
@UIScope
public class MainLayout extends VerticalLayout {

    private final VerticalLayout content = new VerticalLayout();

    @Autowired  private Header header;
    @Autowired private Footer footer;

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
