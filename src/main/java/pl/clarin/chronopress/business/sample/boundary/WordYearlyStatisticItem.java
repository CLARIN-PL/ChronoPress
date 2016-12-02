package pl.clarin.chronopress.business.sample.boundary;

import java.io.Serializable;
import lombok.Data;

@Data
public class WordYearlyStatisticItem implements Serializable {

    private final Integer year;
    private final Long count;

    public WordYearlyStatisticItem(Integer year, Long count) {
        this.year = year;
        this.count = count;
    }
}
