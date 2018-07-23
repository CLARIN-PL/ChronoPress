package pl.clarin.chronopress.presentation.page.admin.audience;

import lombok.Getter;
import pl.clarin.chronopress.business.audience.entity.Audience;

class AudienceSaveEvent {

    @Getter
    private final Audience audience;

    public AudienceSaveEvent(Audience audience) {
        this.audience = audience;
    }

}
