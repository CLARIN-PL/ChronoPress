package pl.clarin.chronopress.business.sample.control;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {

    private final Queue<Long> window = new LinkedList<>();
    private final int period;
    private long sum = 0;

    public MovingAverage(int period) {
        assert period > 0 : "Period must be a positive integer";
        this.period = period;
    }

    public void add(long num) {
        sum = sum + num;
        window.add(num);
        if (window.size() > period) {
            sum = sum - window.remove();
        }
    }

    public long getAverage() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        long divisor = window.size();
        return sum / divisor;
    }
}