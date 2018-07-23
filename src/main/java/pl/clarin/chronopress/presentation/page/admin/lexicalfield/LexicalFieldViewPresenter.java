package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import com.vaadin.cdi.UIScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import pl.clarin.chronopress.business.lexicalfield.boundary.LexicalFieldFacade;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class LexicalFieldViewPresenter extends AbstractPresenter<LexicalFieldView> {

    @Inject
    LexicalFieldFacade facade;
    
    @Override
    protected void onViewEnter() {
        getView().setLexicalNames(facade.findAll());
    }

    public void onSaveLexicalField(@Observes(notifyObserver = Reception.IF_EXISTS) SaveLexicalFieldEvent event) {
        LexicalField lf = facade.save(event.getField());
        getView().swapLexicalName(event.getField(), lf);
    }

    public void onDeleteLexicalField(@Observes(notifyObserver = Reception.IF_EXISTS) DeleteLexicalFieldEvent event) {
        facade.delete(event.getField());
        getView().removeLexicalName(event.getField());
    }
}
