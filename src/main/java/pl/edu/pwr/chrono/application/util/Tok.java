package pl.edu.pwr.chrono.application.util;

import javax.xml.bind.annotation.XmlElement;


public class Tok {

    private String orth;

    private Lex lex;

    @XmlElement
    public String getOrth() {
        return orth;
    }

    public void setOrth(String orth) {
        this.orth = orth;
    }

    @XmlElement
    public Lex getLex() {
        return lex;
    }

    public void setLex(Lex lex) {
        this.lex = lex;
    }
}