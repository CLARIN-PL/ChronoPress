package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

@Data
public class TimeProbe {

    private String lexeme;
    private int month;
    private int year;
    private long count;

    public TimeProbe(String lexeme, int year, int month, long count) {
        this.count = count;
        this.lexeme = lexeme;
        this.month = month;
        this.year = year;
    }

    public TimeProbe(String lexeme, int year, long count) {
        this.count = count;
        this.lexeme = lexeme;
        this.year = year;
    }

}