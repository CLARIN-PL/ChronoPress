package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import lombok.Getter;

public class ShowSampleByIdEvent {

    @Getter
    private final Long id;

    @Getter
    private final String lemma;

    public ShowSampleByIdEvent(Long id, String lemma) {
        this.id = id;
        this.lemma = lemma;
    }

}
