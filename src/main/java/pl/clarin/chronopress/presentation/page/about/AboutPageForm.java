package pl.clarin.chronopress.presentation.page.about;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.education.entity.HomePage;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;

@Slf4j
public class AboutPageForm extends TabSheet {

    @PropertyId("content")
    private final RichTextArea content = new RichTextArea();

    private final TextArea html = new TextArea();

    private final BeanFieldGroup<HomePage> binder = new BeanFieldGroup<>(HomePage.class);

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        binder.bindMemberFields(this);
        setSizeFull();
        
        html.setSizeFull();
        html.setNullRepresentation("");
        html.setCaption("Tryb html");
        
        content.setSizeFull();
        content.setNullRepresentation("");
        content.setCaption("Tryb wizaulany");
                  
        addSelectedTabChangeListener(event -> {
            if (event.getTabSheet().getSelectedTab().equals(html)) {
                html.setValue(content.getValue());
            }
            if (event.getTabSheet().getSelectedTab().equals(content)) {
                content.setValue(html.getValue());
            }

        });
        addComponents(content, html);
    }

    public HomePage getPage() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    public void setPage(HomePage page) {
        binder.setItemDataSource(page);
        content.markAsDirtyRecursive();
    }

    public void cleanUp() {
        binder.discard();
        binder.clear();
    }
}
