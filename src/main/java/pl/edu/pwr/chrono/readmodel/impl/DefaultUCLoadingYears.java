package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.UCLoadingYears;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.List;

/**
 * Created by tnaskret on 07.02.16.
 */
@Service
public class DefaultUCLoadingYears implements UCLoadingYears {

    @Autowired
    private TextRepository repository;

    @Override
    public List<Integer> load() {
        return repository.findYears();
    }
}
