package pl.edu.pwr.chrono.webui.ui.admin.lexicalfield;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.LexicalField;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class LexicalFieldWindow extends Window {

    @PropertyId("groupName")
    private final TextField group = new TextField();
    private final BeanFieldGroup<LexicalField> binder = new BeanFieldGroup<>(LexicalField.class);
    private final Button save = new Button();
    private final Button cancel = new Button();

    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.lexical.field"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(55, Unit.PERCENTAGE);

        save.setCaption(provider.getProperty("button.save"));
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.setIcon(FontAwesome.SAVE);

        cancel.setCaption(provider.getProperty("button.cancel"));
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.setIcon(FontAwesome.TIMES);

        binder.bindMemberFields(this);
        setModal(true);
        center();
        setContent(buildForm());
    }

    public LexicalField getItem() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
        return binder.getItemDataSource().getBean();
    }

    public void setItem(LexicalField lexicalField) {
        binder.setItemDataSource(lexicalField);
    }

    private Component buildForm() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        FormLayout form = new FormLayout();
        form.setWidth(100, Unit.PERCENTAGE);
        form.addStyleName(ChronoTheme.COMPACT_FORM);
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(form);

        group.setCaption(provider.getProperty("label.lexical.group.name"));
        group.setNullRepresentation("");
        form.addComponent(group);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth(100, Unit.PERCENTAGE);
        buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setSpacing(true);
        wrapper.addComponent(save);
        wrapper.addComponent(cancel);
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
}
