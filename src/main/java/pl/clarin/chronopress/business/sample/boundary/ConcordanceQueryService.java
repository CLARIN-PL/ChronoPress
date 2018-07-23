package pl.clarin.chronopress.business.sample.boundary;

import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.business.sample.entity.Sentence;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ConcordanceQueryService {

    private List<Sample> samples = null;

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void initialize() {
       samples = em.createQuery("SELECT s FROM Sample s " +
               "LEFT JOIN FETCH s.sentences se " +
               "LEFT JOIN FETCH se.words", Sample.class)
               .getResultList();
    }

    private List<Sample> findByLemma(String lemma){
        return  samples
                .stream()
                .filter(s -> s.getSentences()
                        .stream()
                        .filter( se -> se.getWords()
                                .stream()
                                .filter( w -> w.getLemma().equals(lemma)).count() > 0).count() > 0)
                .collect(Collectors.toList());
    }


    public List<ConcordanceDTO> findConcordanceByLemma(String lemma) {
      long start = System.currentTimeMillis();
      List<Sample> l = findByLemma(lemma);
      long end = System.currentTimeMillis();
        System.out.println("Execution time: "+ (end - start) +"ms");
        System.out.println(l);
      return Collections.emptyList();
    }


}
