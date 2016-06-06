package pl.edu.pwr.chrono.domain.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

@XmlRootElement(name = "author")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Author  implements Serializable {

    @XmlValue
    private String value;

    public Author(){};
    public Author(String value){
        this.value = value;
    }
}
