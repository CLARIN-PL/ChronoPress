package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Audience;

import java.util.List;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long>, AudienceRepositoryCustom {

    @Query("SELECT DISTINCT a.name FROM Audience a")
    List<String> findAudience();
}
