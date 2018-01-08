package pl.clarin.chronopress.business.sample.control;

import org.junit.Test;
import pl.clarin.chronopress.presentation.shered.dto.TimeProbe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeSeriesCalculatorTest {

    private List<TimeProbe> probes = Arrays.asList(
            new TimeProbe("komunizm", 1945, 2, 2),
            new TimeProbe("komunizm", 1946, 4, 2),
            new TimeProbe("komunizm", 1947, 5, 2),
            new TimeProbe("komunizm", 1951, 2, 2),
            new TimeProbe("komunizm", 1955, 5, 2),
            new TimeProbe("komunizm", 1961, 12, 2),
            new TimeProbe("komunizm", 1961, 02, 2),
            new TimeProbe("scojalizm", 1945, 2, 1),
            new TimeProbe("scojalizm", 1946, 4, 1),
            new TimeProbe("scojalizm", 1947, 5, 1),
            new TimeProbe("scojalizm", 1951, 2, 1),
            new TimeProbe("scojalizm", 1955, 5, 2),
            new TimeProbe("scojalizm", 1961, 12, 1),
            new TimeProbe("scojalizm", 1961, 02, 1)
            );

    @Test
    public void shouldGroupByLemma(){

        TimeSeriesCalculator calculator = new TimeSeriesCalculator(probes);
        Map<Integer, Map<Integer, List<TimeProbe>>> map = calculator.groupByDate(probes);

        assertThat(map.size(), is(equalTo(6)));
    }
}