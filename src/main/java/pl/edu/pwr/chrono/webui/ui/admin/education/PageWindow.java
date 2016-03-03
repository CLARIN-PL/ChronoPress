package pl.edu.pwr.chrono.webui.ui.admin.education;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Page;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PageWindow extends Window {

    @PropertyId("title")
    private final TextField title = new TextField();

    @PropertyId("published")
    private final CheckBox published = new CheckBox();

    @PropertyId("content")
    private final RichTextArea content = new RichTextArea();

    private final BeanFieldGroup<Page> binder = new BeanFieldGroup<>(Page.class);

    private final Button save = new Button();
    private final Button cancel = new Button();
    private final Button delete = new Button();

    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.page"));
        setWidth(80, Unit.PERCENTAGE);
        setHeight(80, Unit.PERCENTAGE);
        addStyleName(ChronoTheme.WINDOW);

        save.setCaption(provider.getProperty("button.save"));
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setIcon(FontAwesome.SAVE);

        cancel.setCaption(provider.getProperty("button.cancel"));
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.setIcon(FontAwesome.TIMES);

        delete.setCaption(provider.getProperty("button.delete"));
        delete.addStyleName(ValoTheme.BUTTON_SMALL);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);
        delete.setIcon(FontAwesome.TRASH_O);

        binder.bindMemberFields(this);
        setModal(true);
        center();
        setContent(buildForm());
    }

    public Page getItem() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
        return binder.getItemDataSource().getBean();
    }

    public void setItem(Page p) {
        binder.setItemDataSource(p);
    }

    private Component buildForm() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        FormLayout form = new FormLayout();
        form.addStyleName(ChronoTheme.COMPACT_FORM);
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(form);
        form.setSizeFull();

        title.setCaption(provider.getProperty("label.page.title"));
        title.setNullRepresentation("");
        form.addComponent(title);

        published.setCaption("label.page.published");
        form.addComponent(published);

        content.setSizeFull();
        content.setNullRepresentation("");
        content.setLocale(MainUI.getCurrent().getLocale());
        layout.addComponent(content);

        layout.setExpandRatio(form, 0.3f);
        layout.setExpandRatio(content, 2);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth(100, Unit.PERCENTAGE);
        buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setSpacing(true);
        wrapper.addComponent(save);
        wrapper.addComponent(cancel);
        wrapper.addComponent(delete);
        buttons.addComponent(wrapper);
        buttons.setComponentAlignment(wrapper, Alignment.MIDDLE_RIGHT);

        layout.addComponent(buttons);

        return layout;
    }

    public Button getSave() {
        return save;
    }

    public Button getCancel() {
        return cancel;
    }

    public Button getDelete() {
        return delete;
    }
}
