package pl.edu.pwr.configuration.profiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile({"development"})
public class DevelopmentProfile {
}
