package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UCLoadingAudience {

    List<String> load();
}
