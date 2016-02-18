package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.readmodel.UCDataExploration;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.WordFrequencyDTO;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.List;

@Service
public class DefaultUCDataExploration implements UCDataExploration {

    @Autowired
    private ListeningExecutorService service;

    @Autowired
    private TextRepository repository;

    @Override
    @Transactional(readOnly = true)
    public ListenableFuture<List<WordFrequencyDTO>> calculateWordFrequencyByLexeme(DataSelectionDTO data) {
        return service.submit(() -> repository.findWordFrequencyByLexeme(data));
    }

    @Override
    public ListenableFuture<List<ConcordanceDTO>> calculateConcordanceNotLemmatized(DataSelectionDTO data, String lemma) {
        return service.submit(() -> repository.findConcordanceNotLemmatized(data, lemma));
    }
}
