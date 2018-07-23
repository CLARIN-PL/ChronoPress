package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.DataExplorationDTO;

public class DataExplorationTab extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataExplorationForm dataExplorationForm;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("view.tab.data.exploration.title"));

        HorizontalLayout layout = new MHorizontalLayout()
                .withMargin(true)
                .withSize(MSize.FULL_SIZE)
                .with(dataExplorationForm)
                .withAlign(dataExplorationForm, Alignment.TOP_CENTER);

        setCompositionRoot(layout);
    }

    public void setConcordanceLemma(String txt) {
        dataExplorationForm.setLemma(txt);
        dataExplorationForm.selectConcordance();
    }

    public DataExplorationDTO getDataExplorationDTO() {
        try {
            return dataExplorationForm.getDataExplorationDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(WordQuantitativeAnalysisTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        dataExplorationForm.reset();
    }
}
