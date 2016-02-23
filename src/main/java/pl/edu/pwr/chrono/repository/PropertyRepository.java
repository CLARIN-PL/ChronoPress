package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Property;

/**
 * Created by tnaskret on 23.02.16.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
