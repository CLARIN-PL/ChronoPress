package pl.clarin.chronopress.presentation.page.start;

import lombok.Getter;

public class SearchAndShowConcordanceEvent {

    @Getter
    private String lemma;

    public SearchAndShowConcordanceEvent(String lemma) {
        this.lemma = lemma;
    }

}
