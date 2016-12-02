/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.property.boundary;

import java.util.Collection;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.property.entity.Property;

@Repository(forEntity = Property.class)
public interface PropertyRepository extends  EntityRepository<Property, Long> {
    
    Collection<Property> findByLangEqual(String lang);
}
