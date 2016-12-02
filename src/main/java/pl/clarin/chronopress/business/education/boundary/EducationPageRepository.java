
package pl.clarin.chronopress.business.education.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.education.entity.EducationPage;

@Repository(forEntity = EducationPage.class)
public interface EducationPageRepository extends  EntityRepository<EducationPage, Long> {
}
