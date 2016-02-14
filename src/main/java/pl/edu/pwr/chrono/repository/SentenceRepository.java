package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Sentence;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Integer>, JpaSpecificationExecutor<Sentence> {
}
