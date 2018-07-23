package pl.clarin.chronopress.business.sample.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dictionary_word_fonems_syllables")
@Data
public class DictionaryWordFonemsAndSyllables implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    private String fonem;

    private String syllables;

    private Integer syllableCount = 0;

    private Integer fonemCount = 0;

    public  DictionaryWordFonemsAndSyllables(){}

    public DictionaryWordFonemsAndSyllables(String word, String fonem, String syllables) {
        this.word = word;
        this.fonem = fonem;
        this.syllables = syllables;
        this.syllableCount = StringUtils.countMatches(syllables.trim(), " ") +1;
        this.fonemCount = StringUtils.countMatches(fonem.trim(), " ") + 1;
    }
}
