package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.UCLoadingTitles;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.List;

/**
 * Created by tnaskret on 07.02.16.
 */
@Service
public class DefaultUCLoadingTitles implements UCLoadingTitles {

    @Autowired
    private TextRepository repository;

    @Override
    public List<String> load() {
        return repository.findJournalTitles();
    }
}
