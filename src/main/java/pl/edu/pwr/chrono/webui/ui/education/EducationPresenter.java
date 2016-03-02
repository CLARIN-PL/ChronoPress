package pl.edu.pwr.chrono.webui.ui.education;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.PageAggregator;
import pl.edu.pwr.chrono.repository.PageAggregatorRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class EducationPresenter extends Presenter<EducationView> {

    @Autowired
    private PageAggregatorRepository repository;

    public List<PageAggregator> getPageAggregators() {
        return repository.findAll();
    }
}
