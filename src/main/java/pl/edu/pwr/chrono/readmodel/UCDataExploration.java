package pl.edu.pwr.chrono.readmodel;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.LexemeProfile;
import pl.edu.pwr.chrono.readmodel.dto.WordFrequencyDTO;
import pl.edu.pwr.chrono.webui.ui.dataanalyse.DataExplorationTab;

import java.util.List;

@Service
public interface UCDataExploration {

    ListenableFuture<List<WordFrequencyDTO>> calculateWordFrequencyByLexeme(DataSelectionDTO data);

    ListenableFuture<List<WordFrequencyDTO>> calculateWordFrequencyNotLematized(DataSelectionDTO data);

    ListenableFuture<List<ConcordanceDTO>> calculateConcordance(DataSelectionDTO data, String lemma);

    ListenableFuture<List<LexemeProfile>> findLexemeProfiles(DataSelectionDTO data, String lemma,
                                                             DataExplorationTab.PartOfSpeech pos, Integer left, Integer right);
}
