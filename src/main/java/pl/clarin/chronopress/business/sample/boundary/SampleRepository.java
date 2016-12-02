package pl.clarin.chronopress.business.sample.boundary;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.sample.entity.Sample;

@Repository(forEntity = SampleRepository.class)
public interface SampleRepository extends EntityRepository<Sample, Long> {

    @Query("SELECT DISTINCT s.journalTitle FROM Sample s")
    List<String> findJournalTitles();

    @Query("FROM Sample s WHERE s.processingStatus =  pl.clarin.chronopress.backend.sample.entity.ProcessingStatus.TO_PROCESS")
    List<Sample> findUnProcessedSamples();

    @Query("SELECT DISTINCT YEAR(t.date) FROM Sample t WHERE t.date is not null")
    List<Integer> findYears();

    @Query("SELECT DISTINCT t.period FROM Sample t WHERE t.period is not null ")
    List<String> findPeriods();

    @Query("SELECT DISTINCT t.exposition FROM Sample t WHERE t.exposition IS NOT NULL ")
    List<Integer> findExpositions();

    @Query("SELECT count(t.id) FROM Sample t")
    int countSamples();

    @Query("SELECT DISTINCT s.authors FROM Sample s WHERE s.authors is not null and s.authors != ''")
    List<String> findAuthors();

    @Query("SELECT t.id FROM Sample t")
    List<Integer> findAllSample();
}
