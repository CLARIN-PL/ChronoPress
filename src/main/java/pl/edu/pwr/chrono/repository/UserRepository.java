package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);

}
