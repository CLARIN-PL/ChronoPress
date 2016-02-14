package pl.edu.pwr.chrono.repository;

import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.List;
import java.util.Optional;

public interface TextRepositoryCustom {

    Optional<DataSelectionResult> countSamplesByCriteria(DataSelectionDTO dto);

    List<SentenceWordCount> findSentenceWordCountAndWordLength(DataSelectionDTO dto);

    List<Word> findWords(DataSelectionDTO selection, QuantitativeAnalysisDTO dto);
}
