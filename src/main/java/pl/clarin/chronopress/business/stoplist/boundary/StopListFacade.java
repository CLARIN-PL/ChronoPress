/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.stoplist.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import pl.clarin.chronopress.business.stoplist.entity.StopList;

@Stateless
public class StopListFacade {

    @Inject
    StopListRepository repository;

    @Transactional
    public StopList save(StopList field) {
        return repository.saveAndFlushAndRefresh(field);
    }

    @Transactional
    public void delete(Long id) {
        repository.remove(repository.findBy(id));
    }

    public List<StopList> findAll() {
        return repository.findAll();
    }

}
