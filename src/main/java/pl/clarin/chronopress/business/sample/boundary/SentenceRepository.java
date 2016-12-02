package pl.clarin.chronopress.business.sample.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.sample.entity.Sentence;

@Repository
public interface SentenceRepository extends EntityRepository<Sentence, Long> {
}
