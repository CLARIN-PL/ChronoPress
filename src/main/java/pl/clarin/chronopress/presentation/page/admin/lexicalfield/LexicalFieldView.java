/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import java.util.List;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

public interface LexicalFieldView extends ApplicationView<LexicalFieldViewPresenter> {
    
    public static final String ID = "lexical-field";
    void setLexicalNames(List<LexicalField> list);
    void swapLexicalName(LexicalField old, LexicalField modified);
    void removeLexicalName(LexicalField filed);
}
