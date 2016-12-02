/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.importer.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;


@XmlRootElement(name = "sentence")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SentenceXml {
    
    @XmlElement(name = "word")
    List<WordXml> words;
    
    @XmlElement(name = "propername")
    List<ProperNameXml> properNames;
    
}
