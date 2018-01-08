package pl.clarin.chronopress.business.sample.control;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import pl.clarin.chronopress.presentation.shered.dto.TimeProbe;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class TimeSeriesCalculator {


    private List<TimeProbe> probes;

    public TimeSeriesCalculator(List<TimeProbe> probes) {
        this.probes = probes;
    }


    protected   Map<Integer, Map<Integer, List<TimeProbe>>> groupByDate(final List<TimeProbe> probes){

        return probes.stream()
                .collect(groupingBy(TimeProbe::getYear, groupingBy(TimeProbe::getMonth)));
    }

    protected List<TimeProbe> countWithSum(Map<Integer, Map<Integer, List<TimeProbe>>> grouped){

        List<TimeProbe> tmp = Lists.newArrayList();
        grouped.forEach((y, map) -> {
            map.forEach((m, t) -> {
                long cnt = t.stream().mapToLong(o -> o.getCount()).sum();
                TimeProbe o = new TimeProbe("Suma próbek", y, m, cnt);
                tmp.add(o);
            });
        });

        return tmp;
    }
    protected   List<TimeProbe> count(Map<Integer, Map<Integer, List<TimeProbe>>> grouped) {
        List<TimeProbe> tmp = Lists.newArrayList();

        grouped.forEach((y, map) -> {
            map.forEach((m, t) -> {
                TimeProbe n = new TimeProbe("", y, m, 0l);
                t.forEach(i -> {
                    n.setLexeme(i.getLexeme());
                    n.setCount(n.getCount() + i.getCount());
                });
                tmp.add(n);
            });
        });

        return tmp;
    }

    protected void sort( List<TimeProbe> list){
        Collections.sort(list, (Object o1, Object o2) -> {

            Integer x1 = ((TimeProbe) o1).getYear();
            Integer x2 = ((TimeProbe) o2).getYear();
            Integer sComp = x1.compareTo(x2);

            if (sComp != 0) {
                return sComp;
            } else {
                Integer z1 = ((TimeProbe) o1).getMonth();
                Integer z2 = ((TimeProbe) o2).getMonth();
                return z1.compareTo(z2);
            }
        });
    }
}
