package pl.edu.pwr.chrono.application.util;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Chunk {

    private List<Sentence> sentence = Lists.newArrayList();

    @XmlElement
    public List<Sentence> getSentence() {
        return sentence;
    }

    public void setSentence(List<Sentence> sentence) {
        this.sentence = sentence;
    }

    public void addSentence(Sentence s) {
        sentence.add(s);
    }
}