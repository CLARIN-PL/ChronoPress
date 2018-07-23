package pl.clarin.chronopress.business.importer.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "sentences")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SentencesXml {

    @XmlElement(name = "sentence")
    private List<SentenceXml> sentences;

}
