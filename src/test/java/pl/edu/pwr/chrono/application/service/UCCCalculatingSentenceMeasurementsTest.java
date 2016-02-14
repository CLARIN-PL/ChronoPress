package pl.edu.pwr.chrono.application.service;

import com.google.common.collect.Lists;
import org.junit.Test;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class UCCCalculatingSentenceMeasurementsTest {

    DefaultUCCalculatingSentenceMeasurements service = new DefaultUCCalculatingSentenceMeasurements();

    @Test
    public void calculateFourthCentralMoment_correctValues_ReturnsValue() {

        List<SentenceWordCount> sentence = Lists.newArrayList();

        SentenceWordCount swc1 = new SentenceWordCount(1, 4L, 20L);
        sentence.add(swc1);

        SentenceWordCount swc2 = new SentenceWordCount(2, 5L, 20L);
        sentence.add(swc2);

        SentenceWordCount swc3 = new SentenceWordCount(2, 6L, 20L);
        sentence.add(swc3);

        DefaultUCCalculatingSentenceMeasurements.Average calculated = service.calculate(sentence, Unit.WORD);
        assertThat(calculated.getAverage()).isEqualTo(5);

        double test = (Math.pow((4 - 5), 4) + Math.pow((5 - 5), 4) + Math.pow((6 - 5), 4)) / 3;
        assertThat(calculated.getFourthCentralMoment()).isEqualTo(test);

    }

    @Test
    public void calculateKurtoze_correctValues_ReturnsValue() {
        List<SentenceWordCount> sentence = Lists.newArrayList();

        SentenceWordCount swc1 = new SentenceWordCount(1, 1L, 20L);
        sentence.add(swc1);

        SentenceWordCount swc2 = new SentenceWordCount(2, 5L, 20L);
        sentence.add(swc2);

        SentenceWordCount swc3 = new SentenceWordCount(2, 3L, 20L);
        sentence.add(swc3);

        DefaultUCCalculatingSentenceMeasurements.Average calculated = service.calculate(sentence, Unit.WORD);

        assertThat(calculated.getAverage()).isEqualTo(3);

        double forthMonent = (Math.pow((1 - 3), 4) + Math.pow((5 - 3), 4) + Math.pow((3 - 3), 4)) / 3;

        assertThat(calculated.getFourthCentralMoment()).isEqualTo(forthMonent);

        double kurtoze = ((forthMonent) / Math.pow(calculated.getStandardDeviation(), 4)) - 3;

        assertThat(calculated.getKurtoze()).isEqualTo(kurtoze);
    }
}
