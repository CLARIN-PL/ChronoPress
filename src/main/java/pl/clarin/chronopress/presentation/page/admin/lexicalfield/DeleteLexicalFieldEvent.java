/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import lombok.Getter;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;

public class DeleteLexicalFieldEvent {

    @Getter
    private final LexicalField field;

    public DeleteLexicalFieldEvent(LexicalField field) {
        this.field = field;
    }
}
