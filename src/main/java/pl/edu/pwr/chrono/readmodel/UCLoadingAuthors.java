package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UCLoadingAuthors {
    List<String> load();
}
