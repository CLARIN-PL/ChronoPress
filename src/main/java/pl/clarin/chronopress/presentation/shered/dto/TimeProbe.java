package pl.clarin.chronopress.presentation.shered.dto;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeProbe {

    private String lexeme;
    private int month;
    private int year;
    private long count;

    public TimeProbe(String lexeme, int year, int month, long count) {
        this.count = count;
        this.lexeme = lexeme.toLowerCase();
        this.month = month;
        this.year = year;
    }

    public TimeProbe(String lexeme, int year, long count) {
        this.count = count;
        this.lexeme = lexeme.toLowerCase();
        this.year = year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.year, this.month, this.lexeme, this.count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeProbe other = (TimeProbe) obj;
        if (this.month != other.month) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.lexeme, other.lexeme)) {
            return false;
        }
        return true;
    }

}
