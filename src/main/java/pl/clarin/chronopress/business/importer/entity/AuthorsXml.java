package pl.clarin.chronopress.business.importer.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "authors")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class AuthorsXml  implements Serializable {

    @XmlElement(name = "author")
    private List<AuthorXml> authors;

    public void addAuthor(AuthorXml author){
        if(authors == null) authors = new ArrayList<>();
        authors.add(author);
    }
}
