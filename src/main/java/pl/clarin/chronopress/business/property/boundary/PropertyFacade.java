/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.property.boundary;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import pl.clarin.chronopress.business.property.entity.Property;

@Stateless
public class PropertyFacade {

    @Inject
    PropertyRepository repository;

    @Transactional
    public Property save(Property prop){
        return repository.save(prop);
    }
    
    public Map<String, String> getPropertiesByLang(String lang) {

        Collection<Property> result = repository.findByLangEqual(lang);

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap();
        result.forEach(p -> map.put(p.getKey(), p.getValue()));

        return Collections.unmodifiableMap(map);
    }
    
    public List<Property> findAll(){
        return repository.findAll();
    }
}
