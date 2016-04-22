package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.domain.ProperName;
import pl.edu.pwr.chrono.domain.Sentence;
import pl.edu.pwr.chrono.domain.SentenceProperName;
import pl.edu.pwr.chrono.readmodel.UCUploadingProperNames;
import pl.edu.pwr.chrono.repository.ProperNameRepository;
import pl.edu.pwr.chrono.repository.SentenceProperNameRepository;
import pl.edu.pwr.chrono.repository.SentenceRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class DefaultUploadingProperNames implements UCUploadingProperNames {

    @Autowired
    private ProperNameRepository properNamesRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private SentenceProperNameRepository sentenceProperNameRepository;

    @Override
    @Transactional
    public void execute(String filename) {
        try {
            Map<String, List<Integer>> names = Maps.newHashMap();
            try (Stream<String> lines = Files.lines(Paths.get(filename), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> process(line, names));
            }
            names.forEach((name, ids) -> {
                ProperName n = new ProperName();
                n.setOriginalProperName(name);

                properNamesRepository.save(n);

                ids.forEach(id -> {
                    Sentence se = sentenceRepository.findOne(id);
                    sentenceProperNameRepository.save(new SentenceProperName(se,n));
                });
            });

        } catch (IOException e) {
           log.debug("Processing file",e);
        }
    }

    private void process(String line,  Map<String, List<Integer>> names ){
        String[] fields = line.split("\t");
        if(fields.length > 0) {
            Integer id = Integer.valueOf(fields[0]);
            for(int i = 1; i < fields.length; i++){
                if(names.containsKey(fields[i])){
                   names.get(fields[i]).add(id);
                } else {
                   names.put(fields[i], Lists.newArrayList());
                   names.get(fields[i]).add(id);
                }
            }
        }
    }
}
