package pl.edu.pwr.chrono.repository.impl;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Audience;
import pl.edu.pwr.chrono.repository.AudienceRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * Created by tnaskret on 08.03.16.
 */
public class AudienceRepositoryImpl implements AudienceRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public Set<String> findAudienceJournalTitles(Set<String> audience) {

        Set<String> result = Sets.newHashSet();
        String query = "FROM Audience a WHERE a.name in :audience";
        List<Audience> audiences = em.createQuery(query)
                .setParameter("audience", audience)
                .getResultList();
        audiences.forEach(a -> result.addAll(a.getJournaltitle()));
        return result;
    }
}
