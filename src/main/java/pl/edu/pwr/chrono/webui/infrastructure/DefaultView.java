package pl.edu.pwr.chrono.webui.infrastructure;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DefaultView<T extends Presenter> extends VerticalLayout {

    protected final T presenter;

    @Autowired
    public DefaultView(T presenter) {
        Responsive.makeResponsive(this);
        setMargin(new MarginInfo(false, true, true, true));
        this.presenter = presenter;
        this.presenter.setView(this);
    }

}
