package pl.clarin.chronopress.business.importer.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

@XmlRootElement(name = "author")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class AuthorXml  implements Serializable {

    @XmlValue
    private String value;

    public AuthorXml(){};
    public AuthorXml(String value){
        this.value = value;
    }
}
