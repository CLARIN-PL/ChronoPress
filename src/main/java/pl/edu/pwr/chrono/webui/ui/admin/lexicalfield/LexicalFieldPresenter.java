package pl.edu.pwr.chrono.webui.ui.admin.lexicalfield;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.LexicalField;
import pl.edu.pwr.chrono.repository.LexicalFieldRepository;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class LexicalFieldPresenter extends Presenter<LexicalFieldView> {

    @Autowired
    private LexicalFieldRepository repository;

    public List<LexicalField> getLexicalFields() {
        return repository.findAll();
    }

    public void save(LexicalField field) {
        repository.save(field);
    }

    public void delete(LexicalField lexicalField) {
        repository.delete(lexicalField);
    }
}
