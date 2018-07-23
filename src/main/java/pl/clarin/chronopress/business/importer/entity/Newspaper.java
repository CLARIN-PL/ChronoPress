package pl.clarin.chronopress.business.importer.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "newspaper")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Newspaper {

    @XmlElement(name = "title_newspaper")
    private List<NewspaperTitle> newspaperTitles;

    public void addNewspaperTitle(NewspaperTitle title){
        if(newspaperTitles == null) newspaperTitles = new ArrayList<>();
        newspaperTitles.add(title);
    }
}
