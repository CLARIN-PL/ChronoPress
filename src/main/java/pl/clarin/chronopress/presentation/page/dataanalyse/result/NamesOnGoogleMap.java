package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.SimpleGeolocation;

public class NamesOnGoogleMap implements CalculationResult {

    private final GoogleMap googleMap = new GoogleMap("AIzaSyAeZBiABB9m2_k6kRJt7fboKw2iGYxfXnU", null, null);
    private final VerticalLayout panel = new VerticalLayout();

    @Inject
    private DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ShowConcordanceWindowEvent> showCorcondance;

    @PostConstruct
    public void init() {
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
        googleMap.addMarkerClickListener(l -> {
            String base = l.getCaption().substring(l.getCaption().indexOf("[base:") + 6, l.getCaption().lastIndexOf("]"));
            showCorcondance.fire(new ShowConcordanceWindowEvent(base));
        });
        panel.addComponent(googleMap);
    }

    public void addData(List<SimpleGeolocation> results) {
        results.forEach(c -> {
            String icon = "http://ws.clarin-pl.eu/public/icons/point-1-10.png";
            if (c.getFreq() >= 10 && c.getFreq() < 100) {
                icon = "http://ws.clarin-pl.eu/public/icons/point-10-100.png";
            }
            if (c.getFreq() >= 100 && c.getFreq() < 1000) {
                icon = "http://ws.clarin-pl.eu/public/icons/point-100-1000.png";
            }
            if (c.getFreq() >= 1000) {
                icon = "http://ws.clarin-pl.eu/public/icons/point-1000.png";
            }
            String desc = c.getDisplay_name() + " [frekw:" + c.getFreq() + "][base:" + c.getBase() + "]";
            googleMap.addMarker(desc, new LatLon(c.getLat(), c.getLon()), false, icon);
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
