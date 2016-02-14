package pl.edu.pwr.chrono.application.service.impl;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.service.UCCalculatingSentenceMeasurements;
import pl.edu.pwr.chrono.infrastructure.Unit;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

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

        public void accept(long i) {
            elementSum += i;
            elementSquareSum += i * i;
            total++;
        }

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
            return Math.sqrt(n * (getSquareAverage() - a));
        }

        public double getCoefficientOfVariation(){
            return  getAverage() > 0 ? getStandardDeviation() / getAverage() : 0;
        }
    }
}
