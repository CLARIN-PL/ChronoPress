package pl.clarin.chronopress.business.property.boundary;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbPropertiesProvider {

    @Inject
    PropertyFacade facade;
    
    private Map<String, String> settings;

    @PostConstruct
    public void init(){
        loadProperties();
    }
    
    public void loadProperties() {
        settings = facade.getPropertiesByLang("PL");
    }

    public String getProperty(String key) {
        String property = settings.get(key);
        if (property == null) {
            property = key;
        }
        return property;
    }
}
