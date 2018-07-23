package pl.clarin.chronopress.presentation.page.about;

import pl.clarin.chronopress.business.education.entity.HomePage;

public class SaveAboutPageEvent {

    private final HomePage page;

    SaveAboutPageEvent(HomePage page) {
        this.page = page;
    }

    public HomePage getPage() {
        return page;
    }
}
