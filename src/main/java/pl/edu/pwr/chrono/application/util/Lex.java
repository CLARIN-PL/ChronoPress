package pl.edu.pwr.chrono.application.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Lex {


    private String base;

    @XmlAttribute
    private String disamb = "1";


    private String ctag;

    @XmlElement
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @XmlElement
    public String getCtag() {
        return ctag;
    }

    public void setCtag(String ctag) {
        this.ctag = ctag;
    }
}