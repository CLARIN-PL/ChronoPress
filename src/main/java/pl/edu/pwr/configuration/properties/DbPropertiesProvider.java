package pl.edu.pwr.configuration.properties;

import org.springframework.stereotype.Component;
import pl.edu.pwr.chrono.domain.Property;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DbPropertiesProvider {

	@PersistenceContext
	private EntityManager em;

	private Map<String, String> settings;

    @PostConstruct
    public void loadProperties(){
        settings = loadPropertiesFromDB("PL");
    }

    private Map loadPropertiesFromDB(String lang) {
        String  query = "FROM Property p  WHERE p.lang = :lang";

        List<Property> result = em.createQuery(query)
                            .setParameter("lang", lang)
                            .getResultList();

        ConcurrentHashMap map = new ConcurrentHashMap();

        result.forEach(p -> map.put(p.getKey(), p.getValue()));
        return Collections.unmodifiableMap(map);
    }

	public String getProperty(String key){
        String property = settings.get(key);
        if(property == null){
            property = key;
        }
		return property;
	}
}