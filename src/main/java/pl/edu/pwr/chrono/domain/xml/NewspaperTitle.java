package pl.edu.pwr.chrono.domain.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title_newspaper")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class NewspaperTitle {

    @XmlValue
    private String value;

    public NewspaperTitle(){};
    public NewspaperTitle(String value){
        this.value = value;
    }
}
