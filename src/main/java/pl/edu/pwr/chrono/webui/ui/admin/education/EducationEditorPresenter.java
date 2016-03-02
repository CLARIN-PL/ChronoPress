package pl.edu.pwr.chrono.webui.ui.admin.education;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Page;
import pl.edu.pwr.chrono.domain.PageAggregator;
import pl.edu.pwr.chrono.repository.PageAggregatorRepository;
import pl.edu.pwr.chrono.repository.PageRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class EducationEditorPresenter extends Presenter<EducationEditorView> {

    @Autowired
    private PageAggregatorRepository repository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private DbPropertiesProvider provider;

    public List<PageAggregator> getPageAggregators() {
        return repository.findAll();
    }

    public void savePage(Page p) {
        pageRepository.save(p);
    }

    public void savePageAggregator(PageAggregator p) {
        repository.save(p);
    }

    public void removePageAggregator(PageAggregator p) {
        repository.delete(p);
    }

    public void removePage(Page p) {
        pageRepository.delete(p);
    }
}
