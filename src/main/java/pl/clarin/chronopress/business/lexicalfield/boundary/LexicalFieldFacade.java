package pl.clarin.chronopress.business.lexicalfield.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;

@Stateless
public class LexicalFieldFacade {
    
    @Inject
    LexicalFieldRepository repository;
    
    public List<LexicalField> findAll(){
        return repository.findAll();
    }
    
    public LexicalField save(LexicalField lf){
        return  repository.save(lf);
    }
    
    public void delete(LexicalField lf){
        repository.removeAndFlush(repository.findBy(lf.getId()));
    }
}
