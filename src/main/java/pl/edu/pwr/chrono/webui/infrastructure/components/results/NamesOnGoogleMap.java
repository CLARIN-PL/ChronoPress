package pl.edu.pwr.chrono.webui.infrastructure.components.results;


import com.google.common.collect.Lists;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;


public class NamesOnGoogleMap implements CalculationResult {

    final GoogleMap googleMap = new GoogleMap("", null, "polish");
    private final VerticalLayout panel = new VerticalLayout();
    private DbPropertiesProvider provider;


    public NamesOnGoogleMap(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeMap();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setHeight(500, Sizeable.Unit.PIXELS);
        panel.setCaption(provider.getProperty("label.names.map"));
        panel.setMargin(new MarginInfo(false, false, true, false));

    }

    private void initializeMap() {
        googleMap.setSizeFull();
        googleMap.setZoom(7);
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        panel.addComponent(googleMap);
    }

    public void loadData(List<String> names) {

        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDjMHzltiFKBTJeMmuvbNr9jel7xtA3YWM");
        List<GeocodingResult> results = Lists.newArrayList();
        names.forEach(s -> {
            try {
                GeocodingResult[] r = GeocodingApi.geocode(context, s).await();
                for (int i = 0; i < r.length; i++) {
                    results.add(r[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        results.forEach(geocodingResult -> {
            googleMap.addMarker(geocodingResult.formattedAddress,
                    new LatLon(geocodingResult.geometry.location.lat,
                            geocodingResult.geometry.location.lng), false, null);
        });

    }


    @Override
    public String getType() {
        return "googleNames";
    }

    @Override
    public Component showResult() {
        return panel;
    }

}
