package pl.edu.pwr.chrono.readmodel.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.edu.pwr.chrono.domain.ProperName;
import pl.edu.pwr.chrono.readmodel.UCProcessGeolocations;
import pl.edu.pwr.chrono.readmodel.dto.Geolocation;
import pl.edu.pwr.chrono.repository.ProperNameRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DefaultUCProcessGeolocations implements UCProcessGeolocations{

    @Autowired
    private ProperNameRepository repository;

    @Value("${geolocation}")
    private String url;

    @Override
    public void process() {

        List<ProperName> names = repository.findNotProcessedGeolocation();

        RestTemplate rest = new RestTemplate();

        names.forEach(n -> {
            final String uri = url + "/search.php?q=" + n.getOriginalProperName() + "&format=json";

            try {
                log.info(uri);
                String json = rest.getForObject(uri, String.class);

                List<Geolocation> list = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();

                //convert JSON string to list
                list = mapper.readValue(json, new TypeReference<ArrayList<Geolocation>>() {});
                if(list.size() > 0){
                    if(list.get(0).getType().equals("city") ||
                            list.get(0).getType().equals("village") ||
                            list.get(0).getType().equals("administrative")){

                        n.setType(list.get(0).getType());
                        n.setAlias(list.get(0).getDisplay_name());
                        n.setLat(list.get(0).getLat());
                        n.setLon(list.get(0).getLon());
                        n.setProcessed(true);
                        repository.save(n);
                    }
                }

            } catch (Exception e) {
                log.info("Exception converting {} to map",  e);
            }

        });
    }
}
