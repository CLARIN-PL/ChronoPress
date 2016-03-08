package pl.edu.pwr.chrono.readmodel.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.UCLoadingAudience;
import pl.edu.pwr.chrono.repository.AudienceRepository;

import java.util.List;

@Service
public class DefaultUCLoadingAudience implements UCLoadingAudience {

    @Autowired
    private AudienceRepository repository;

    @Override
    public List<String> load() {
        return repository.findAudience();
    }
}
