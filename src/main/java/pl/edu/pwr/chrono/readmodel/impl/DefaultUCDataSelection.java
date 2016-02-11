package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.readmodel.UCDataSelection;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.repository.SentenceRepository;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.chrono.repository.TextSpecification;

import java.util.List;
import java.util.Optional;

/**
 * Created by tnaskret on 05.02.16.
 */

@Service
public class DefaultUCDataSelection implements UCDataSelection {


    @Autowired
    private TextRepository textRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private ListeningExecutorService service;

    @Override
    @Transactional(readOnly = true)
    public ListenableFuture<Optional<DataSelectionResult>> search(DataSelectionDTO dto) {

      return service.submit(() -> {
           //TODO add collect only id
           List<Text> texts = textRepository.findAll(TextSpecification.search(dto));
           List<Integer> ids = Lists.newArrayList();
           texts.forEach(i -> {
               ids.add(i.getId());
           });

          //TODO Ansyc
           final List<Integer> resultCount = Lists.newArrayList();
           if(!ids.isEmpty()) {
                List<List<Integer>> smallerLists = Lists.partition(ids, 20);
                if (!smallerLists.isEmpty()) {
                    smallerLists.forEach( l -> {
                        resultCount.add(sentenceRepository.findWordCount(l));
                    });
                }
            }
           DataSelectionResult result = new DataSelectionResult(ids, texts.size(),
                   resultCount.stream().mapToInt(Integer::intValue).sum());
           return  Optional.of(result);
       });

    }
}
