package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.SimpleGeolocation;

import java.util.List;

@Service
public interface UCLoadGoogleLocations {

    List<SimpleGeolocation> load(DataSelectionDTO dto);

}
