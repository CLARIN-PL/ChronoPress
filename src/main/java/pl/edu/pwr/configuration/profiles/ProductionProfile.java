package pl.edu.pwr.configuration.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"production"})
public class ProductionProfile {
}
