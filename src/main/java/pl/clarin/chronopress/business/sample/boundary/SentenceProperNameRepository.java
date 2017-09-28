package pl.clarin.chronopress.business.sample.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.sample.entity.SentenceProperName;

@Repository
public interface SentenceProperNameRepository extends EntityRepository<SentenceProperName, Long> {

    @Query("FROM SentenceProperName spn WHERE spn.sentence.id = :sentenceId AND spn.properName.id = :properNameId")
    SentenceProperName findBySentenceAndProperName(@QueryParam("sentenceId") Long sentenceId, @QueryParam("properNameId") Long properNameId);
}
