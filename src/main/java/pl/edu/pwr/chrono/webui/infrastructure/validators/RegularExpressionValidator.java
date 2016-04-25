package pl.edu.pwr.chrono.webui.infrastructure.validators;

import com.vaadin.data.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

@Component
public class RegularExpressionValidator implements Validator {

    @Autowired
    private DbPropertiesProvider provider;

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (value != null && !"".equals(value.toString())) {
            boolean matches = value.toString().matches("[\\w%_ążłćźóę ]+");
            if (!matches)
                throw new InvalidValueException(provider.getProperty("validation.contains.invalid.characters"));
        }
    }
}
