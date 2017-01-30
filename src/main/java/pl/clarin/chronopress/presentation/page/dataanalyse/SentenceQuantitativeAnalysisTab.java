/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;

public class SentenceQuantitativeAnalysisTab extends CustomComponent {

    @Inject
    SentenceAnalysisForm sentenceAnalysisForm;

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption("Analizuj zdania");

        HorizontalLayout layout = new MHorizontalLayout()
                .withFullWidth()
                .with(sentenceAnalysisForm)
                .withAlign(sentenceAnalysisForm, Alignment.TOP_CENTER);
        setCompositionRoot(layout);
    }

    public SentenceAnalysisDTO getSentenceAnalysisDTO() {
        try {
            return sentenceAnalysisForm.getSentenceAnalysisDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(WordQuantitativeAnalysisTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        sentenceAnalysisForm.reset();
    }
}
