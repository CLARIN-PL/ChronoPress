package pl.edu.pwr.chrono.webui.infrastructure.validators;

import com.vaadin.data.Validator;

public class RegularExpressionValidator implements Validator {

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (value != null && !"".equals(value.toString())) {
            boolean matches = value.toString().matches("[\\w%_]+");
            if (!matches) throw new InvalidValueException("Contains forbidden characters");
        }
    }
}
