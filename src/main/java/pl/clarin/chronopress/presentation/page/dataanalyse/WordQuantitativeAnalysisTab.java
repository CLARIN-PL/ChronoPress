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
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;

public class WordQuantitativeAnalysisTab extends CustomComponent {

    @Inject
    WordAnalysisFrom wordAnalysisFrom;

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("view.tab.word.quantitative.analysis.title"));

        HorizontalLayout layout = new MHorizontalLayout()
                .withFullWidth()
                .with(wordAnalysisFrom)
                .withAlign(wordAnalysisFrom, Alignment.TOP_CENTER);

        setCompositionRoot(layout);
    }

    public WordAnalysisDTO getWordAnalysisDTO() {
        try {
            return wordAnalysisFrom.getWordAnalysisDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(WordQuantitativeAnalysisTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void reset(){
        wordAnalysisFrom.reset();
    }
}
