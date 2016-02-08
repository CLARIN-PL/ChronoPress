package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tnaskret on 08.02.16.
 */
@Service
public interface UCLoadingAuthors {
    List<String> load();
}
