package pl.edu.pwr.chrono.domain.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Sample implements Serializable {

    @XmlElement
    private Newspaper newspaper;

    @XmlElement
    private String title_article;

    @XmlElement
    private Authors authors ;

    @XmlElement
    private String language;

    @XmlElement
    private String style;

    @XmlElement
    private String year;

    @XmlElement
    private String month;

    @XmlElement
    private String day;

    @XmlElement
    private Date date;

    @XmlElement
    private String period;

    @XmlElement
    private String status;

    @XmlElement
    private String support;

    @XmlElement
    private String exposition;

    @XmlElement
    @XmlJavaTypeAdapter(value=CDATAAdapter.class)
    private String text = "";

}
