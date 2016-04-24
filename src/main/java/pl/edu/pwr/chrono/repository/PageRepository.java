package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.chrono.domain.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("FROM Page p WHERE p.category = 'home'")
    Page findHomePage();

}
