package pl.clarin.chronopress.presentation.shered.layout;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.ComboBox;
import java.util.List;
import java.util.Locale;
import pl.clarin.chronopress.business.shered.Identifiable;

public class MComboBox<T extends Identifiable<Long>> extends ComboBox {

    private final BeanContainer<Long, T> container;

    public MComboBox(String property, Class<T> clazz) {

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
                if (value != null) {
                    return container.getItem(value).getBean();
                }
                return null;
            }

            @Override
            public Object convertToPresentation(Object value, Class targetType,
                    Locale locale) throws ConversionException {
                if (value != null) {
                    return ((T) value).getId();
                }
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

    public void removeBean(T bean) {
        container.removeItem(bean.getId());
    }

    public void removeWithUnselect(T bean) {
        unselect(bean.getId());
        container.removeItem(bean.getId());
    }

    public T getBean() {
        if (getValue() != null) {
            return (T) container.getItem(getValue()).getBean();
        }
        return null;
    }

    public void addBean(T bean) {
        container.addBean(bean);

    }

    public void addBeans(List<T> list) {
        container.removeAllItems();
        container.addAll(list);
    }
}
