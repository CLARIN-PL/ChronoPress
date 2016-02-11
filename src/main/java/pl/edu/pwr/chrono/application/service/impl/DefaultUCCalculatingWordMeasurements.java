package pl.edu.pwr.chrono.application.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingWordMeasurements;
import pl.edu.pwr.chrono.domain.Word;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

/**
 * Created by tnaskret on 09.02.16.
 */
@Service
public class DefaultUCCalculatingWordMeasurements implements UCCalculatingWordMeasurements {

    public enum Unit {LETTER, SYLLABLE}

    @Override
    public Average calculate(final List<Word> list, Unit unit) {
        if(list == null) return new Average();
        Average average = averageCalculations(list, unit);
        return  average;
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
    public Map<Integer, Long> averageLengthHistogram(final List<Word> list){
       Map<Integer , Long> map = list.stream()
                .collect(Collectors.groupingBy(o -> o.getTxt().length(),
                        Collectors.counting()));
        return sortByIntegerKey(map);
    }

    @Override
    public Map<Long, Long> frequencyHistogram(final List<Word> list){
        Map<String , Long> map = list.stream()
                .collect(Collectors.groupingBy(o -> o.getTxt(), Collectors.counting()));
        Map<Long, Long> result = map.entrySet()
                                    .stream()
                                    .collect(Collectors.groupingBy(e -> e.getValue(), Collectors.counting()));
        return sortByLongKey(result);
    }

    public static Map sortByIntegerKey(Map<Integer, Long> map){
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static Map sortByLongKey(Map<Long, Long> map){
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public class Average implements IntConsumer
    {
        private int elementSum = 0;
        private int elementSquareSum = 0;
        private int total = 0;

        public void accept(int i) { elementSum += i; elementSquareSum += i*i; total++;}

        public void combine(Average other) {
            elementSum += other.elementSum;
            elementSquareSum += other.elementSquareSum;
            total +=  other.total;
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
            double deviation = Math.sqrt(n*(getSquareAverage() - a));
            return deviation;
        }

        public double getCoefficientOfVariation(){
            return  getAverage() > 0 ? getStandardDeviation() / getAverage() : 0;
        }
    }
}
