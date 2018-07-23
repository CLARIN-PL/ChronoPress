package pl.clarin.chronopress.business.calculations.control;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm.PartOfSpeech;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.FrequencyItem;
import pl.clarin.chronopress.presentation.shered.dto.LexemeProfile;

@Stateless
public class DataExploration {

    @Inject
    SampleFacade facade;

    public List<FrequencyItem> calculateWordFrequencyByLexeme(DataSelectionDTO data) {
        return facade.findWordFrequencyByLexeme(data);
    }

    public List<FrequencyItem> calculateWordFrequencyNotLematized(DataSelectionDTO data) {
        return facade.findWordFrequencyNotLemmatized(data);
    }

    public List<ConcordanceDTO> calculateConcordance(DataSelectionDTO data, String lemma, Boolean caseSensitive) {
        return splitSentenceOnWord(facade.findConcordance(data, lemma, caseSensitive));
    }

    public List<ConcordanceDTO> calculateConcordance(String lemma) {
        return splitSentenceOnWord(facade.findConcordanceByLemma(lemma));
    }

    public List<LexemeProfile> findLexemeProfiles(DataSelectionDTO data, String lemma, PartOfSpeech pos, Integer left, Integer right, Boolean caseSensitive) {
        return facade.findLexemeProfile(data, lemma, pos, left, right, caseSensitive);
    }

    private List<ConcordanceDTO> splitSentenceOnWord(final List<ConcordanceDTO> list) {
        final List<ConcordanceDTO> split = Lists.newArrayList();
        list.forEach(s -> {
            int occurrence = StringUtils.countMatches(s.getSentence(), s.getWord());
            int lastPosition = 0;
            for (int i = 0; i < occurrence; i++) {
                ConcordanceDTO d = new ConcordanceDTO(s);

                lastPosition = d.getSentence().indexOf(s.getWord(), lastPosition);
                d.setLeft((d.getSentence().substring(0, lastPosition)).trim());
                d.setRight((d.getSentence().substring(lastPosition + d.getWord().length(), d.getSentence().length())).trim());

                lastPosition++;
                split.add(d);
            }
        });

        return split;
    }

    public List<ConcordanceDTO> calculateConcordance(String base, LocalDate date) {
        return splitSentenceOnWord(facade.findConcordanceByLemma(base, date));
    }
}
