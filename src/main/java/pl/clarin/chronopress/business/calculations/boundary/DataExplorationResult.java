package pl.clarin.chronopress.business.calculations.boundary;

import java.util.List;
import lombok.Data;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.dto.FrequencyItem;
import pl.clarin.chronopress.presentation.shered.dto.LexemeProfile;
import pl.clarin.chronopress.presentation.shered.dto.SimpleGeolocation;

@Data
public class DataExplorationResult {

    private List<FrequencyItem> wordFrequencyByLexeme;
    private List<FrequencyItem> wordFrequencyNotLematized;
    private List<ConcordanceDTO> concordance;
    private String lemma;
    private List<LexemeProfile> profile;
    private List<SimpleGeolocation> geolocations;

}
