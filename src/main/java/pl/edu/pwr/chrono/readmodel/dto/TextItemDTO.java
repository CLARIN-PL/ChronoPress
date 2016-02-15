package pl.edu.pwr.chrono.readmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TextItemDTO {

    private Integer id;
    private String source;
    private String title;
    private String author;
    private Date publishDate;
    private String style;

    public TextItemDTO(Integer id, String source, String title, String author, Date publishDate, String style) {
        this.id = id;
        this.source = source;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.style = style;
    }
}
