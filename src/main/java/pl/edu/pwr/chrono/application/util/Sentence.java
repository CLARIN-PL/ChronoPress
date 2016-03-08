package pl.edu.pwr.chrono.application.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;


public class Sentence {

    private String id;
    private List<Tok> tok = new ArrayList<>();

    public Sentence(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    public List<Tok> getTok() {
        return tok;
    }

    public void setTokens(List<Tok> tokens) {
        this.tok = tokens;
    }

    public void addToken(String orth, String base, String ctag) {
        Tok t = new Tok();
        t.setOrth(orth);
        Lex l = new Lex();
        l.setBase(base);
        l.setCtag(ctag);
        t.setLex(l);
        tok.add(t);
    }
}