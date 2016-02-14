package pl.edu.pwr.chrono.application.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Unit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;


@Service
public class DefaultUCCalculatingWordMeasurements implements UCCalculatingWordMeasurements {

    public static Map sortByLongKey(Map<Long, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Average calculate(final List<Word> list, Unit unit) {
        if(list == null) return new Average();
        return averageCalculations(list, unit);
    }

    private Average averageCalculations(final List<Word> list, Unit unit){
        if(unit == Unit.LETTER) {
            return list.stream()
                    .filter(i -> i.getTxt() != null)
                    .map(i -> i.getTxt().length())
                    .collect(Average::new, Average::accept, Average::combine);
        }
        return new Average();
    }

    @Override
    public Map averageLengthHistogram(final List<Word> list) {
        Map<Long, Long> map = list.stream()
                .filter(i -> i.getTxt() != null)
                .collect(Collectors.groupingBy(o -> (long) o.getTxt().length(),
                        Collectors.counting()));
        return sortByLongKey(map);
    }

    @Override
    public Map frequencyHistogram(final List<Word> list) {
        Map<String , Long> map = list.stream()
                .collect(Collectors.groupingBy(Word::getTxt, Collectors.counting()));
        Map<Long, Long> result = map.entrySet()
                                    .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        return sortByLongKey(result);
    }

    public class Average implements LongConsumer
    {
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
            double n = (double)total/(total -1);
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

        public double getKurtoze() {
            return getFourthCentralMoment() / Math.pow(getStandardDeviation(), 4) - 3;
        }
    }
}
