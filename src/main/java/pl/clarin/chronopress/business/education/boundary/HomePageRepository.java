
package pl.clarin.chronopress.business.education.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.education.entity.HomePage;

@Repository(forEntity = HomePage.class)
public interface HomePageRepository extends  EntityRepository<HomePage, Long> {
}
