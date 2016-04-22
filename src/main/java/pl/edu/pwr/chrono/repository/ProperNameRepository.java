package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.ProperName;

import java.util.List;

@Repository
public interface ProperNameRepository extends JpaRepository<ProperName, Long>{

    @Query("FROM ProperName p WHERE p.processed = false")
    List<ProperName> findNotProcessedGeolocation();

}
