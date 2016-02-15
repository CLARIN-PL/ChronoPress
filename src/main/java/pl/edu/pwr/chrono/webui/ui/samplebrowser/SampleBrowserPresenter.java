package pl.edu.pwr.chrono.webui.ui.samplebrowser;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.LazyList;
import pl.edu.pwr.chrono.readmodel.dto.TextItemDTO;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class SampleBrowserPresenter extends Presenter<SampleBrowserView> implements LazyList.EntityProvider<TextItemDTO> {

    @Autowired
    private TextRepository repository;

    @Override
    public int size() {
        return repository.countByTexts();
    }

    @Override
    public List findEntities(int firstRow) {
        return repository.findTextItem(firstRow);
    }
}
