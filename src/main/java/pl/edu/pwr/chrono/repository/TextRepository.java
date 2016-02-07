package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Text;

import java.util.List;

/**
 * Created by tnaskret on 05.02.16.
 */
@Repository
public interface TextRepository extends JpaRepository<Text, Integer>, JpaSpecificationExecutor<Text> {

    @Query("SELECT DISTINCT YEAR(t.date) FROM Text t")
    List<Integer> findYears();

    @Query("SELECT DISTINCT t.period FROM Text  t")
    List<String> findPeriods();

    @Query("SELECT DISTINCT t.title_j FROM Text t")
    List<String> findJournalTitles();

    @Query("SELECT DISTINCT t.exposition FROM Text t WHERE t.exposition IS NOT NULL ")
    List<Integer> findExpositions();

}
