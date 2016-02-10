package pl.edu.pwr.chrono.application.service.impl;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

/**
 * Created by tnaskret on 10.02.16.
 */
@Service
public class DefaultUCCalculatingSentenceMeasurements implements UCCalculatingSentenceMeasurements{

    public enum Unit {LETTER, WORD}

    @Override
    public Average calculate(final List<SentenceWordCount> list,  Unit unit) {
        if(list == null) return new Average();
        Average average = averageCalculations(list, unit);
        return  average;
    }

    private Average averageCalculations(final List<SentenceWordCount> list, Unit unit){
        if(unit == Unit.WORD) {
            return list.stream()
                    .map(i-> i.getWordCount())
                    .collect(Average::new, Average::accept, Average::combine);
        }
        if(unit == Unit.LETTER){
            return list.stream()
                    .map(i-> i.getLetterCount())
                    .collect(Average::new, Average::accept, Average::combine);
        }
        return new Average();
    }

    @Override
    public Map<Integer, Long> frequencyCalculations(final List<SentenceWordCount> list, Unit unit){

        if(unit == Unit.WORD) {
            Map<Integer, Long> map = list.stream()
                    .collect(Collectors.groupingBy(o -> o.getWordCount(),
                            Collectors.counting()));
            return map;
        }
        if(unit == Unit.LETTER){
            Map<Integer, Long> map = list.stream()
                    .collect(Collectors.groupingBy(o -> o.getLetterCount(),
                            Collectors.counting()));
            return map;
        }
        return Maps.newConcurrentMap();
    }

    public class Average implements IntConsumer{

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
