package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.ui.HorizontalLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class Header extends HorizontalLayout {

    @Inject
    MainMenu menu;

    @Inject
    Branding branding;

    @PostConstruct
    public void init() {
        setWidth(100, Unit.PERCENTAGE);
        addStyleName(ChronoTheme.HEADER);
        addComponents(branding, menu);
    }
}
