/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.audience.boundary;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.business.sample.boundary.SampleRepository;

@Stateless
public class AudienceFacade {
    
    @Inject
    AudienceRepository repository;
    
    @Inject
    SampleRepository sampleRepository;
    
    @Inject
    EntityManager em;
            
    public List<String> findJournalNames(){
        return sampleRepository.findJournalTitles();
    }
    
    public Audience save(Audience audience){
       return repository.saveAndFlushAndRefresh(audience);
    }
    
    public List<Audience> findAll(){
        return  repository.findAll();
    }
    
    public List<String> findAudience(){
        return repository.findAudience();
    }
    
    public void remove(Audience audience){
        repository.removeAndFlush(repository.findBy(audience.getId()));
    }
    
    public Set<String> findAudienceJournalTitles(Set<String> audience) {

        Set<String> result = Sets.newHashSet();
        String query = "FROM Audience a WHERE a.audienceName in :audience";
        List<Audience> audiences = em.createQuery(query)
                .setParameter("audience", audience)
                .getResultList();
        audiences.forEach(a -> result.addAll(a.getJournalTitle()));
        return result;
    }
}
