package pl.edu.pwr.chrono.webui.infrastructure.components.results;


import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import pl.edu.pwr.chrono.readmodel.dto.SimpleGeolocation;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;


public class NamesOnGoogleMap implements CalculationResult {

    final GoogleMap googleMap = new GoogleMap(null, null, null);
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
        googleMap.setZoom(4);
        googleMap.setMinZoom(0);
        googleMap.setMaxZoom(16);
        panel.addComponent(googleMap);
    }

    public void loadData(List<SimpleGeolocation> results) {
        results.forEach( c -> {
            String icon = "http://ws.clarin-pl.eu/public/icons/point-1-10.png";
            if(c.getFreq() >= 10 && c.getFreq() < 100) icon = "http://ws.clarin-pl.eu/public/icons/point-10-100.png";
            if(c.getFreq() >= 100 && c.getFreq() < 1000) icon = "http://ws.clarin-pl.eu/public/icons/point-100-1000.png";
            if(c.getFreq() >= 1000) icon = "http://ws.clarin-pl.eu/public/icons/point-1000.png";

            googleMap.addMarker(c.getDisplay_name()+ " ["+c.getFreq()+"]",
                                new LatLon(c.getLat(), c.getLon()), false, icon);
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
