package pl.edu.pwr.chrono.application.service.impl;

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
    public Average calculateByWord(final List<SentenceWordCount> list) {
        if(list == null) return new Average();
        Average average = averageCalculations(list, Unit.WORD);
        return  average;
    }

    private Average averageCalculations(final List<SentenceWordCount> list, Unit unit){
        if(unit == Unit.WORD) {
            return list.stream()
                    .map(i-> i.getWordCount())
                    .collect(Average::new, Average::accept, Average::combine);
        }
        return new Average();
    }

    @Override
    public Map<Integer, Long> frequencyCalculationsByWord(final List<SentenceWordCount> list){
        Map<Integer , Long> map = list.stream()
                .collect(Collectors.groupingBy(o -> o.getWordCount(),
                        Collectors.counting()));
        return map;
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
