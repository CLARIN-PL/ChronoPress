package pl.edu.pwr.chrono.webui.ui.admin.caption;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Property;
import pl.edu.pwr.chrono.webui.infrastructure.components.EditSubSection;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

/**
 * Created by tnaskret on 03.01.16.
 */

@SpringComponent
@ViewScope
public class CaptionEditSubSection extends CustomComponent {

    @Autowired
    private DbPropertiesProvider properties;

    private BeanFieldGroup<Property> binder;

    private Button save, cancel;

    private EditSubSection section;

    @PropertyId("key")
    private TextField key = new TextField();

    @PropertyId("value")
    private TextField value = new TextField();

    @PostConstruct
    private void init() {
        binder = new BeanFieldGroup<>(Property.class);

        initializeButtons();
        initializeFields();
        initializeEditSection();

        setCompositionRoot(section);
        binder.bindMemberFields(this);
    }

    private void initializeButtons() {
        save = new Button();
        cancel = new Button();
    }

    private void initializeFields() {
        value.setColumns(20);
        key.setColumns(25);
        key.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        key.setEnabled(false);
    }

    private void initializeEditSection() {
        section = new EditSubSection.EditSubSectionBuilder()
                .addRow(new EditSubSection.FieldBuilder()
                                .addField(null, key)
                                .addField(null, value)
                                .addButton(properties.getProperty("label.save"),
                                        FontAwesome.SAVE, save)
                                .addButton(properties.getProperty("label.cancel"),
                                        FontAwesome.BAN, cancel)
                                .build()
                )
                .buildWithoutHeader();
    }

    public void commit() throws FieldGroup.CommitException {
        binder.commit();
    }

    public Property getPropertySetting() {
        return binder.getItemDataSource().getBean();
    }

    public void setPropertySetting(Property dto) {
        binder.setItemDataSource(dto);
    }

    public void show() {
        setVisible(true);
        key.setReadOnly(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void setSaveButtonClickListener(final Button.ClickListener listener) {
        save.addClickListener(listener);
    }

    public void setCancelButtonClickListener(final Button.ClickListener listener) {
        cancel.addClickListener(listener);
    }
}
