package pl.edu.pwr.chrono.webui.infrastructure.validators;

import com.vaadin.data.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pwr.Application;
import pl.edu.pwr.configuration.datasource.DataSourceConfig;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DataSourceConfig.class})
public class RegularExpressionValidatorTest {

    @Autowired
    private RegularExpressionValidator validator;

    @Test
    public void validate_correctValues_NoException() throws Exception {

        String test = "%_ski%";

        try {
            validator.validate(test);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }


    @Test
    public void validate_acceptsPolishCharacters_NoException() throws Exception {

        String test = "ążłćźóę";

        try {
            validator.validate(test);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test(expected = Validator.InvalidValueException.class)
    public void validate_inCorrectValues_ThrowsException() throws Exception {
        String test = "%_#'':ski";
        validator.validate(test);
    }
}