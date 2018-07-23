package pl.clarin.chronopress.business.sample.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.sample.entity.DictionaryWordFonemsAndSyllables;

@Repository(forEntity = DictionaryWordFonemsAndSyllables.class)
public interface DictionaryWordFonemsAndSyllablesRepository extends EntityRepository<DictionaryWordFonemsAndSyllables, Integer> {
}
