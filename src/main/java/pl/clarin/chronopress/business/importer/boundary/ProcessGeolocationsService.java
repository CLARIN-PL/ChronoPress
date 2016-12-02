package pl.clarin.chronopress.business.importer.boundary;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import pl.clarin.chronopress.business.propername.boundary.ProperNameFacade;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.StartGeolocationProcessingEvent;
import pl.clarin.chronopress.presentation.shered.dto.Geolocation;

@ApplicationScoped
public class ProcessGeolocationsService {

    public static final String URL = "http://maps.clarin-pl.eu/";

    @Inject
    ProperNameFacade facade;

    @Inject
    @Dedicated
    ExecutorService executor;

    public void onProcessGeolocations(@Observes StartGeolocationProcessingEvent event) {

        List<ProperName> names = facade.findNotProcessedGeolocation();
        Logger.getLogger(ProcessGeolocationsService.class.getName()).log(Level.INFO, "Geolocations to process = " + names.size());

        List<List<ProperName>> partitoned = Lists.partition(new ArrayList(names), 1000);

        partitoned.forEach(l -> {

            CompletableFuture.supplyAsync(() -> {
                processChunk(l);
                return 0;
            }, executor).thenRun(() -> {
                Logger.getLogger(ProcessSampleService.class.getName()).log(Level.INFO, "Process zako\u0144czony ");
            });
        });
    }

    private void processChunk(List<ProperName> list) {
        Client client = ClientBuilder.newClient();
        list.forEach(n -> {
            try {
                String uri = URL + "/search.php?q=" + n.getBase() + "&format=json";
                if (n.getUserCorrection() != null) {
                    uri = URL + "/search.php?q=" + n.getUserCorrection() + "&format=json";
                }
                String json = getRes(client.target(uri).request().get());

                ObjectMapper mapper = new ObjectMapper();

                List<Geolocation> geo = new ArrayList<>();    //convert JSON string to list
                geo = mapper.readValue(json, new TypeReference<ArrayList<Geolocation>>() {
                });

                if (geo.size() > 0) {
                    if (geo.get(0).getType().equals("city")
                            || geo.get(0).getType().equals("village")
                            || geo.get(0).getType().equals("administrative")) {

                        n.setNameOnMap(geo.get(0).getDisplay_name());
                        n.setLat(geo.get(0).getLat());
                        n.setLon(geo.get(0).getLon());
                        n.setProcessed(true);
                        facade.save(n);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ProcessGeolocationsService.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    private String getRes(Response res) throws IOException {
        if (res.getStatus() != 200) {
            throw new IOException("Error in nlprest processing");
        }
        return res.readEntity(String.class);
    }
}
