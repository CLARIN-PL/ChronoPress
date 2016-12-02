package pl.clarin.chronopress.presentation.shered.validators;

import com.vaadin.data.Validator;
import javax.inject.Inject;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;

public class RegularExpressionValidator implements Validator {

    @Inject
    DbPropertiesProvider provider;

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (value != null && !"".equals(value.toString())) {
            boolean matches = value.toString().matches("[\\w%_ążłćźóę ]+");
            if (!matches)
                throw new InvalidValueException(provider.getProperty("validation.contains.invalid.characters"));
        }
    }
}
