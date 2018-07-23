package pl.clarin.chronopress.business.sample.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class AverageLongCalulator implements LongConsumer {

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

    public void combine(AverageLongCalulator other) {
        elementSum += other.elementSum;
        elementSquareSum += other.elementSquareSum;
        total += other.total;
        elements = other.elements;
    }

    public double getAverage() {
        return total > 0 ? (double) elementSum / total : 0;
    }

    public double getSquareAverage() {
        return total > 0 ? (double) elementSquareSum / total : 0;
    }

    public double getStandardDeviation() {
        if (total <= 0) {
            return 0;
        }
        double n = (double) total / (total - 1);
        double a = getAverage() * getAverage();
        return Math.sqrt(n * (getSquareAverage() - a));
    }

    public double getCoefficientOfVariation() {
        return getAverage() > 0 ? getStandardDeviation() / getAverage() : 0;
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
        return getFourthCentralMoment() / Math.pow(getStandardDeviation(), 4) - 3;
    }
}
