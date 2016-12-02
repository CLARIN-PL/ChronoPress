package pl.clarin.chronopress.business.propername.boundary;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.propername.entity.ProperName;

@Repository(forEntity = ProperName.class)
public interface ProperNameRepository extends EntityRepository<ProperName, Long> {   
    
    @Query("FROM ProperName pn WHERE pn.orth = :orth and pn.type = :type")
    ProperName findByOrthAndType(@QueryParam("orth") String orth, @QueryParam("type") String type);
    
    @Query("FROM ProperName pn WHERE pn.type LIKE 'nam_loc%' AND pn.processed = false")
    List<ProperName> findGeoloctionProperNamesNotProcessed();
    
    @Query("FROM ProperName pn WHERE pn.type LIKE 'nam_loc%'")
    List<ProperName> findGeoloctionProperNames();
    
}
