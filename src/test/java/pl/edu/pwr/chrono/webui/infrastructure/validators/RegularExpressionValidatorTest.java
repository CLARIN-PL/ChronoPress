package pl.edu.pwr.chrono.webui.infrastructure.validators;

import com.vaadin.data.Validator;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by tnaskret on 14.02.16.
 */
public class RegularExpressionValidatorTest {

    @Test
    public void validate_correctValues_NoException() throws Exception {
        RegularExpressionValidator validator = new RegularExpressionValidator();

        String test = "%_ski%";

        try {
            validator.validate(test);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test(expected = Validator.InvalidValueException.class)
    public void validate_inCorrectValues_ThrowsException() throws Exception {
        String test = "%_#'':ski";
        RegularExpressionValidator validator = new RegularExpressionValidator();
        validator.validate(test);
    }
}