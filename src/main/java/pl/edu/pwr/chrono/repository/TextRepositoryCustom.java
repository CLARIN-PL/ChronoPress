package pl.edu.pwr.chrono.repository;

import pl.edu.pwr.chrono.application.util.WordToCllDTO;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.dto.*;

import java.util.List;
import java.util.Optional;

public interface TextRepositoryCustom {

    Optional<DataSelectionResult> countSamplesByCriteria(DataSelectionDTO dto);

    List<SentenceWordCount> findSentenceWordCountAndWordLength(DataSelectionDTO dto, QuantitativeAnalysisDTO analysisDTO);

    List<Word> findWords(DataSelectionDTO selection, QuantitativeAnalysisDTO dto);

    TimeSeriesResult findTimeSeries(final DataSelectionDTO selection, final TimeSeriesDTO dto);

    List<WordFrequencyDTO> findWordFrequencyByLexeme(DataSelectionDTO selection);

    List<WordFrequencyDTO> findWordFrequencyNotLemmatized(DataSelectionDTO selection);

    List<ConcordanceDTO> findConcordance(DataSelectionDTO selection, String lemma);

    List<WordToCllDTO> findWordCCL(Integer textId);
}
