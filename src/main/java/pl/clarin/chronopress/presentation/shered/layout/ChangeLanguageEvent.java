package pl.clarin.chronopress.presentation.shered.layout;

import lombok.Getter;

/**
 *
 * @author tnaskret
 */
public class ChangeLanguageEvent {

    @Getter
    private String lnag;

    public ChangeLanguageEvent(String lnag) {
        this.lnag = lnag;
    }

}
