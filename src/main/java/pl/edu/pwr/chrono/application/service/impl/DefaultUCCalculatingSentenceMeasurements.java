package pl.edu.pwr.chrono.application.service.impl;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static pl.edu.pwr.chrono.application.service.impl.DefaultUCCalculatingWordMeasurements.sortByLongKey;

@Service
public class DefaultUCCalculatingSentenceMeasurements implements UCCalculatingSentenceMeasurements{

    @Override
    public Average calculate(final List<SentenceWordCount> list,  Unit unit) {
        if(list == null) return new Average();
        return averageCalculations(list, unit);
    }

    private Average averageCalculations(final List<SentenceWordCount> list, Unit unit){
        if(unit == Unit.WORD) {
            return list.stream()
                    .map(SentenceWordCount::getWordCount)
                    .collect(Average::new, Average::accept, Average::combine);
        }
        if(unit == Unit.LETTER){
            return list.stream()
                    .map(SentenceWordCount::getLetterCount)
                    .collect(Average::new, Average::accept, Average::combine);
        }
        return new Average();
    }

    @Override
    public Map frequencyCalculations(final List<SentenceWordCount> list, Unit unit) {

        if(unit == Unit.WORD) {
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(SentenceWordCount::getWordCount,
                            Collectors.counting()));
            return sortByLongKey(map);
        }
        if(unit == Unit.LETTER){
            Map<Long, Long> map = list.stream()
                    .collect(Collectors.groupingBy(SentenceWordCount::getLetterCount,
                            Collectors.counting()));
            return sortByLongKey(map);
        }
        return Maps.newConcurrentMap();
    }

    public class Average implements LongConsumer {

        private long elementSum = 0;
        private long elementSquareSum = 0;
        private long total = 0;
        private List<Long> elements = new ArrayList<>();

        public void accept(long i) {
            elementSum += i;
            elementSquareSum += i * i;
            total++;
            elements.add(i);
        }

        public void combine(Average other) {
            elementSum += other.elementSum;
            elementSquareSum += other.elementSquareSum;
            total +=  other.total;
            elements = other.elements;
        }

        public double getAverage(){
            return total > 0 ? (double)elementSum / total : 0;
        }

        public double getSquareAverage(){
            return total > 0 ? (double)elementSquareSum / total: 0;
        }

        public double getStandardDeviation(){
            if (total <= 0) return 0;
            double n = (double) total / (total - 1);
            double a = getAverage()*getAverage();
            return Math.sqrt(n * (getSquareAverage() - a));
        }

        public double getCoefficientOfVariation(){
            return  getAverage() > 0 ? getStandardDeviation() / getAverage() : 0;
        }

        public double getFourthCentralMoment() {
            final long[] fourthCentralMoment = {0};
            elements.forEach(i -> fourthCentralMoment[0] += Math.pow((i - getAverage()), 4));
            return (double) fourthCentralMoment[0] / total;
        }

        public double getMedian() {
            Collections.sort(elements);
            double median = 0;
            double centralElementsAverage = 0.0;

            if (elements.size() % 2 == 0) {
                centralElementsAverage = elements.get(elements.size() / 2) + elements.get((elements.size() / 2) - 1);
                median = centralElementsAverage / 2.0;

            } else {
                median = elements.get(elements.size() / 2);
            }
            return median;
        }

        public double getSkewness() {
            return (3 * (getAverage() - getMedian())) / getStandardDeviation();
        }

        public double getKurtoze() {
            return (getFourthCentralMoment() / Math.pow(getStandardDeviation(), 4)) - 3;
        }
    }
}
