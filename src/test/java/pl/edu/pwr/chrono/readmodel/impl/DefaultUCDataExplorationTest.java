package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import org.junit.Test;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by tnaskret on 22.02.16.
 */
public class DefaultUCDataExplorationTest {

    private DefaultUCDataExploration service = new DefaultUCDataExploration();

    @Test
    public void splitSentenceByWord_SentenceWithWordOccurredOnce_SplitSentence() {

        String sentence = "janusz miał dużo pracy i robił w polu cały dzień";

        List<ConcordanceDTO> list = Lists.newArrayList();
        ConcordanceDTO dto = new ConcordanceDTO("pracy", "pracy", sentence, new Date(), "Trybuna", "", "", "", "");
        list.add(dto);

        List<ConcordanceDTO> after = service.splitSentenceOnWord(list);
        assertThat("janusz miał dużo").isEqualTo(after.get(0).getLeft());
        assertThat("i robił w polu cały dzień").isEqualTo(after.get(0).getRight());
    }

    @Test
    public void splitSentenceByWord_SentenceWithWordOccurredTwice_SplitSentence() {

        String sentence = "janusz miał dużo pracy i robił w pracy cały dzień, żeby zarobić";

        List<ConcordanceDTO> list = Lists.newArrayList();
        ConcordanceDTO dto = new ConcordanceDTO("pracy", "pracy", sentence, new Date(), "Trybuna", "", "", "", "");
        list.add(dto);

        List<ConcordanceDTO> after = service.splitSentenceOnWord(list);
        assertThat("janusz miał dużo").isEqualTo(after.get(0).getLeft());
        assertThat("i robił w pracy cały dzień, żeby zarobić").isEqualTo(after.get(0).getRight());

        assertThat("janusz miał dużo pracy i robił w").isEqualTo(after.get(1).getLeft());
        assertThat("cały dzień, żeby zarobić").isEqualTo(after.get(1).getRight());
    }
}