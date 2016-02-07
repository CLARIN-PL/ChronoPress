package pl.edu.pwr.chrono.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tnaskret on 05.02.16.
 */

@Entity
@Table(name = "text")
@Data
public class Text implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "textSeq")
    @SequenceGenerator(name = "textSeq", sequenceName = "text_id_seq")
    private Integer id;

    @Column(name = "shortid")
    private String shortId;

    private String title_j;
    private String title_a;

    @Column(name = "authors_parsed", columnDefinition = "text")
    private String authors;

    private String lang;

    private String style;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_parsed")
    private Date date;

    private String period;

    private String status;

    private String medium;

    @Column(name = "file_name" ,columnDefinition = "text")
    private String fileName;

    @Column(name = "txt" ,columnDefinition = "text")
    private String txt;

    @Column(columnDefinition = "int2")
    private Integer exposition;
}
