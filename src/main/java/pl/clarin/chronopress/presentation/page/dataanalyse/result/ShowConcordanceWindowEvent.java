package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;

public class ShowConcordanceWindowEvent implements Serializable {

    @Getter
    private String base;

    @Getter
    private LocalDate date;

    public ShowConcordanceWindowEvent(String base) {
        this.base = base;
    }

    public ShowConcordanceWindowEvent(String base, LocalDate date) {
        this.base = base;
        this.date = date;
    }
}
