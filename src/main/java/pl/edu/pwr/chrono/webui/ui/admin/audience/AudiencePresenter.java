package pl.edu.pwr.chrono.webui.ui.admin.audience;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Audience;
import pl.edu.pwr.chrono.repository.AudienceRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class AudiencePresenter extends Presenter<AudienceView> {

    @Autowired
    private AudienceRepository repository;

    public void save(Audience audience) {
        repository.save(audience);
    }

    public List<Audience> getAudience() {
        return repository.findAll();
    }
}
