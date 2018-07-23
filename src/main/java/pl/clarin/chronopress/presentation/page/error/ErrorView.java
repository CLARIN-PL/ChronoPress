package pl.clarin.chronopress.presentation.page.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;

public class ErrorView extends CustomComponent implements View {

    @Inject
    DbPropertiesProvider provider;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        Label label = new Label(provider.getProperty("security.no.access"));

        MVerticalLayout layout = new MVerticalLayout()
                .withSize(MSize.FULL_SIZE)
                .withMargin(true)
                .with(label)
                .withAlign(label, Alignment.MIDDLE_CENTER);

        setCompositionRoot(layout);
    }
}
