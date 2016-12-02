package pl.clarin.chronopress.presentation.page.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class ErrorView extends CustomComponent implements View {
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        
        Label label = new Label("Could not find a view with that name. You are most likely doing it wrong.");
        
        MVerticalLayout layout = new MVerticalLayout()
            .withSize(MSize.FULL_SIZE)
            .withMargin(true)
            .with(label)
            .withAlign(label, Alignment.MIDDLE_CENTER);
        
        setCompositionRoot(layout);
    }
}