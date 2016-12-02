package pl.clarin.chronopress.business.audience.boundary;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.audience.entity.Audience;

@Repository(forEntity = Audience.class)
public interface AudienceRepository extends EntityRepository<Audience, Long> {

    @Query("SELECT DISTINCT a.audienceName FROM Audience a")
    List<String> findAudience();
}
