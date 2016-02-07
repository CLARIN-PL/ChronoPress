package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Sentence;

import java.util.List;

/**
 * Created by tnaskret on 05.02.16.
 */
@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Integer>{

    @Query("SELECT sum(s.wordCount) FROM Sentence s WHERE s.text.id in :textIds ")
    Integer findWordCount(@Param("textIds") List<Integer> textIds);

}
