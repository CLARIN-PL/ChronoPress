package pl.edu.pwr.chrono.webui.ui.admin;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Page;
import pl.edu.pwr.chrono.repository.PageRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

@SpringComponent
@UIScope
@Slf4j
public class AdminPresenter extends Presenter<AdminView> {

    @Autowired
    public PageRepository repository;

    public Page getHomePage(){
        return repository.findHomePage();
    }

    public void  save(Page p){
        repository.save(p);
    }
}
