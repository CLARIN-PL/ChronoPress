package pl.edu.pwr.chrono.webui.ui.admin.education;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.PageAggregator;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class CategoryWindow extends Window {

    @PropertyId("title")
    private final TextField title = new TextField();

    private final BeanFieldGroup<PageAggregator> binder = new BeanFieldGroup<>(PageAggregator.class);

    private final Button save = new Button();
    private final Button cancel = new Button();

    @Autowired
    private DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("label.window.page.category"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(35, Unit.PERCENTAGE);
        setHeight(21, Unit.PERCENTAGE);

        save.setCaption(provider.getProperty("button.save"));
        cancel.setCaption(provider.getProperty("button.cancel"));

        binder.bindMemberFields(this);
        setModal(true);
        center();
        setContent(buildForm());
    }

    public PageAggregator getItem() {
        try {
            binder.commit();
            return binder.getItemDataSource().getBean();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
        return binder.getItemDataSource().getBean();
    }

    public void setItem(PageAggregator p) {
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

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth(100, Unit.PERCENTAGE);
        buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setSpacing(true);
        wrapper.addComponent(cancel);
        wrapper.addComponent(save);
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
