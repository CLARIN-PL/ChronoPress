package pl.edu.pwr.chrono.webui.ui.samplebrowser;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class SampleBrowserPresenter extends Presenter<SampleBrowserView> {

    @Autowired
    private TextRepository repository;

    public long size() {
        return repository.count();
    }

    public List<Text> findEntities(int firstRow, boolean asc, String sortProperty) {
        return repository.findAllBy(
                new PageRequest(
                        firstRow / TextRepository.PAGE_SIZE,
                        TextRepository.PAGE_SIZE,
                        asc ? Sort.Direction.ASC : Sort.Direction.DESC,
                        // fall back to id as "natural order"
                        sortProperty == null ? "id" : sortProperty
                ));
    }
}
