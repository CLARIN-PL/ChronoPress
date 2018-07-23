package pl.clarin.chronopress.business.lexicalfield.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;

@Repository(forEntity = LexicalField.class)
public interface LexicalFieldRepository extends EntityRepository<LexicalField, Long>{
}
