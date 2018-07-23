package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class Branding extends HorizontalLayout {

    public Branding() {
        setSpacing(false);
        addStyleName(ChronoTheme.BRANDING);
        //layout.addComponents(addImage("img/logo_uw.png", 45), addImage("img/logo-clarin.png", 45));
        addComponents(addImage("img/logo.png", 45));

    }

    private Image addImage(String path, int height) {
        Image img = new Image(null, new ThemeResource(path));
        img.setHeight(height, Unit.PIXELS);
        return img;
    }
}
