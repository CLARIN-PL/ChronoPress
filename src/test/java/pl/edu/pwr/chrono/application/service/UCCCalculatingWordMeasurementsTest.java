package pl.edu.pwr.chrono.application.service;

import com.google.common.collect.Lists;
import org.junit.Test;
import pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Unit;

import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class UCCCalculatingWordMeasurementsTest {

    DefaultUCCalculatingWordMeasurements service = new DefaultUCCalculatingWordMeasurements();

    @Test
    public void calculateAverageLength_ListSizeZero_ReturnsZero(){

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(Lists.newArrayList(), Unit.LETTER);
        assertThat(calculated.getAverage()).isEqualTo(0);

    }

    @Test
    public void calculateAverageLength_SumOfWordsIsZero_ReturnsZero(){

        List<Word> words = Lists.newArrayList();
        Word w1 = new Word();
        w1.setTxt("");
        words.add(w1);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getAverage()).isEqualTo(0);

    }

    @Test
    public void calculateAverageLength_CorrectValues_ReturnsCalculatedValue(){

        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("krowa"); //5
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("GAZETA RObotnicza"); //17
        words.add(w3);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getAverage()).isEqualTo(9);

    }

    @Test
    public void calculateAverageLength_WordIsNull_ReturnsCalculatedValue(){

        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        words.add(w2);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getAverage()).isEqualTo(5f);

    }

    @Test
    public void calculateStandardDeviation_CorrectValues_ReturnsCalculatedValue(){

        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("zamek"); //5
        words.add(w2);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getStandardDeviation()).isEqualTo(0.0);

    }

    @Test
    public void calculateStandardDeviation_WordIsNull_ReturnsCalculatedValue(){

        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("zamek"); //3
        words.add(w2);

        Word w3 = new Word();
        words.add(w1);

        Word w4 = new Word();
        w4.setTxt("zapał"); //7
        words.add(w2);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getStandardDeviation()).isEqualTo(0.0);

    }

    @Test
    public void calculateCoefficientOfVariation_CorrectValues_ReturnsCalculatedValue(){
        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getCoefficientOfVariation()).isEqualTo(Math.sqrt(2)/4);
    }

    @Test
    public void calculateWordAverageLengthHistogram_CorrectValues_ReturnsHistogramMap(){
        List<Word> words = Lists.newArrayList();

        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("dom"); //3
        words.add(w2);

        Map calculated = service.averageLengthHistogram(words);
        assertThat(calculated).containsKey(3L);
        assertThat(calculated).containsKey(5L);
        assertThat(calculated).containsValue(2L);
        assertThat(calculated).containsValue(1L);
    }

    @Test
    public void calculateMedian_CorrectValuesEvenListSize_ReturnsmMedianValue() {
        List<Word> words = Lists.newArrayList();
        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("dom"); //3
        words.add(w3);

        Word w4 = new Word();
        w4.setTxt("dom"); //3
        words.add(w4);

        Word w5 = new Word();
        w5.setTxt("Krowa"); //5
        words.add(w5);

        Word w6 = new Word();
        w6.setTxt("pies"); //5
        words.add(w6);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getMedian()).isEqualTo(3.5);
    }

    @Test
    public void calculateMedian_CorrectValuesOddListSize_ReturnsmMedianValue() {
        List<Word> words = Lists.newArrayList();
        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("dom"); //3
        words.add(w3);

        Word w4 = new Word();
        w4.setTxt("dom"); //3
        words.add(w4);

        Word w5 = new Word();
        w5.setTxt("Krowa"); //5
        words.add(w5);

        Word w6 = new Word();
        w6.setTxt("pies"); //5
        words.add(w6);

        Word w7 = new Word();
        w7.setTxt("łopatka"); //5
        words.add(w7);

        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        assertThat(calculated.getMedian()).isEqualTo(4);
    }

    @Test
    public void calculateWordFrequencyHistogram_CorrectValues_ReturnsFrequencyMap(){
        List<Word> words = Lists.newArrayList();
        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("dom"); //3
        words.add(w3);

        Word w4 = new Word();
        w4.setTxt("dom"); //3
        words.add(w4);

        Word w5 = new Word();
        w5.setTxt("Krowa"); //5
        words.add(w5);

        Word w6 = new Word();
        w6.setTxt("pies"); //5
        words.add(w6);

        Word w7 = new Word();
        w7.setTxt("pies"); //5
        words.add(w7);

        Word w8 = new Word();
        w8.setTxt("zamek"); //5
        words.add(w8);

        Map calculated = service.frequencyHistogram(words);
    }

    @Test
    public void calculateSkewness_CorrectValuesOddListSize_ReturnsSkewnessValue() {
        List<Word> words = Lists.newArrayList();
        Word w1 = new Word();
        w1.setTxt("zamek"); //5
        words.add(w1);

        Word w2 = new Word();
        w2.setTxt("dom"); //3
        words.add(w2);

        Word w3 = new Word();
        w3.setTxt("dom"); //3
        words.add(w3);

        Word w4 = new Word();
        w4.setTxt("dom"); //3
        words.add(w4);

        Word w5 = new Word();
        w5.setTxt("Krowa"); //5
        words.add(w5);

        Word w6 = new Word();
        w6.setTxt("pies"); //5
        words.add(w6);

        Word w7 = new Word();
        w7.setTxt("łopatka"); //5
        words.add(w7);


        DefaultUCCalculatingWordMeasurements.Average calculated = service.calculate(words, Unit.LETTER);
        double skewness = (3 * (calculated.getAverage() - calculated.getMedian())) / calculated.getStandardDeviation();
        assertThat(calculated.getSkewness()).isEqualTo(skewness);
    }
}
