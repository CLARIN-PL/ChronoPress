package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.readmodel.UCDataSelection;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.repository.SentenceRepository;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.chrono.repository.TextSpecification;

import java.util.List;

/**
 * Created by tnaskret on 05.02.16.
 */

@Service
public class DefaultDataSelection implements UCDataSelection {


    @Autowired
    private TextRepository textRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Override
    public DataSelectionResult search(DataSelectionDTO dto) {

       List<Text> texts =  textRepository.findAll(TextSpecification.search(dto));
       List<Integer> ids = Lists.newArrayList();
       texts.forEach(i -> {ids.add(i.getId());});

       Integer wordCount = sentenceRepository.findWordCount(ids);
       DataSelectionResult result = new DataSelectionResult(ids, texts.size(), wordCount);

        System.out.println(textRepository.findYears());
        System.out.println(textRepository.findPeriods());
        System.out.println(textRepository.findJournalTitles());
        System.out.println(textRepository.findExpositions());

        return  result;
    }
}
