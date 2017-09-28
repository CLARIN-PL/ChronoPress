package pl.clarin.chronopress.business.importer.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "word")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class WordXml {

    @XmlElement
    private String orth;

    @XmlElement
    private String base;

    @XmlElement
    private String ctag;

}
