package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.UCLoadingPeriods;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.List;

/**
 * Created by tnaskret on 07.02.16.
 */
@Service
public class DefaultUCLoadingPeriods implements UCLoadingPeriods {

    @Autowired private TextRepository repository;

    @Override
    public List<String> load() {
        return repository.findPeriods();
    }
}
