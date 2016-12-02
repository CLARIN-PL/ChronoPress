/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.stoplist.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.stoplist.entity.StopList;

@Repository(forEntity = StopList.class)
public interface StopListRepository extends EntityRepository<StopList, Long> {
}
