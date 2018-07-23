package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class Footer extends HorizontalLayout{

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init(){
        setWidth(100, Sizeable.Unit.PERCENTAGE);
        addStyleName(ChronoTheme.FOOTER);

        HorizontalLayout content = initializeFooter();
        addComponent(content);
        setComponentAlignment(content , Alignment.BOTTOM_CENTER);
    }


    private HorizontalLayout initializeFooter(){
        HorizontalLayout layout = new HorizontalLayout();

        Label label = new Label(provider.getProperty("footer.copy.right"));
        label.addStyleName(ValoTheme.LABEL_COLORED);
        layout.addComponent(label);
        return layout;
    }

}
