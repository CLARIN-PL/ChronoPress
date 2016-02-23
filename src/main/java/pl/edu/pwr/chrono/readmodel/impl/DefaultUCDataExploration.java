package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.apache.commons.lang3.StringUtils;
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
    public ListenableFuture<List<WordFrequencyDTO>> calculateWordFrequencyNotLematized(DataSelectionDTO data) {
        return service.submit(() -> repository.findWordFrequencyNotLemmatized(data));
    }

    @Override
    public ListenableFuture<List<ConcordanceDTO>> calculateConcordanceNotLemmatized(DataSelectionDTO data, String lemma) {
        return service.submit(() -> splitSentenceOnWord(repository.findConcordanceNotLemmatized(data, lemma)));
    }

    public List<ConcordanceDTO> splitSentenceOnWord(final List<ConcordanceDTO> list) {
        final List<ConcordanceDTO> split = Lists.newArrayList();
        list.forEach(s -> {
            int occurrence = StringUtils.countMatches(s.getSentence(), s.getBase());
            int lastPosition = 0;
            for (int i = 0; i < occurrence; i++) {
                ConcordanceDTO d = new ConcordanceDTO(s);

                lastPosition = d.getSentence().indexOf(s.getBase(), lastPosition);
                d.setLeft((d.getSentence().substring(0, lastPosition)).trim());
                d.setRight((d.getSentence().substring(lastPosition + d.getBase().length(), d.getSentence().length())).trim());

                lastPosition++;
                split.add(d);
            }
        });
        return split;
    }
}
