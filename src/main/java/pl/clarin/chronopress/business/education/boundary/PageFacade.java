package pl.clarin.chronopress.business.education.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.education.entity.HomePage;
import pl.clarin.chronopress.business.education.entity.TitleDTO;

@Stateless
public class PageFacade {

    @Inject
    EducationPageRepository repository;

    @Inject
    HomePageRepository homeRepository;

    @PersistenceContext
    EntityManager em;

    public List<EducationPage> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void savePage(EducationPage page) {
        repository.saveAndFlushAndRefresh(page);
    }

    @Transactional
    public void removePage(EducationPage page) {
        EducationPage p = repository.findBy(page.getId());
        repository.remove(p);
    }

    public Map<String, List<TitleDTO>> getPagesCategorisWithTitles() {
        final Map<String, List<TitleDTO>> map = new HashMap<>();
        List<TitleDTO> dtos = em.createQuery("SELECT NEW "
                + "pl.clarin.chronopress.business.education.entity.TitleDTO(ep.id, ep.category, ep.pageTitle) "
                + "FROM EducationPage ep").getResultList();

        dtos.forEach(t -> {
            if (!map.containsKey(t.getCategory())) {
                map.put(t.getCategory(), new ArrayList<>());
            }
            map.get(t.getCategory()).add(t);
        });
        return map;
    }

    public EducationPage getPageById(Long id) {
        return repository.findBy(id);
    }

    public HomePage getHomePage() {
        List<HomePage> list = homeRepository.findAll();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new HomePage();
    }

    public HomePage saveHomePage(HomePage page) {
        return homeRepository.saveAndFlushAndRefresh(page);
    }
}
