package pl.clarin.chronopress.presentation.page.admin.propernames;

import lombok.Getter;
import pl.clarin.chronopress.business.propername.entity.ProperName;

public class ShowProperNameEvent {
     @Getter
    private ProperName properName;

    public ShowProperNameEvent(ProperName properName) {
        this.properName = properName;
    }
}
