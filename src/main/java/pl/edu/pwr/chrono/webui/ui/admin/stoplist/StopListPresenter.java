package pl.edu.pwr.chrono.webui.ui.admin.stoplist;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.StopList;
import pl.edu.pwr.chrono.repository.StopListRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class StopListPresenter extends Presenter<StopListView> {

    @Autowired
    private StopListRepository repository;

    public List<StopList> getStopList() {
        return repository.findAll();
    }

    public void save(StopList field) {
        repository.save(field);
    }

    public void delete(StopList field) {
        repository.delete(field);
    }
}
