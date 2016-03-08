package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.ComboBox;
import pl.edu.pwr.chrono.infrastructure.Identifiable;

import java.util.List;
import java.util.Locale;

public class EntityComboBox<T extends Identifiable<Long>> extends ComboBox {

    private final BeanContainer<Long, T> container;
    private final Class<T> clazz;

    public EntityComboBox(String property, Class<T> clazz) {
        this.clazz = clazz;
        container = new BeanContainer<>(clazz);
        setContainerDataSource(container);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        container.setBeanIdProperty("id");
        setItemCaptionPropertyId(property);
        setImmediate(true);
        setNullSelectionAllowed(false);
        setTextInputAllowed(false);
        setConverter(new Converter() {

            @Override
            public Object convertToModel(Object value, Class targetType,
                                         Locale locale) throws ConversionException {
                if (value != null)
                    return container.getItem(value).getBean();
                return null;
            }

            @Override
            public Object convertToPresentation(Object value, Class targetType,
                                                Locale locale) throws ConversionException {
                if (value != null)
                    return ((T) value).getId();
                return null;
            }

            @Override
            public Class<T> getModelType() {
                return clazz;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }

        });
    }

    public BeanContainer<Long, T> getContainer() {
        return container;
    }

    public void load(List<T> list) {
        container.removeAllItems();
        container.addAll(list);
    }
}
