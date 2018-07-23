package pl.clarin.chronopress.presentation.page.about;

import pl.clarin.chronopress.business.education.entity.HomePage;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface AboutView extends ApplicationView<AboutViewPresenter> {

    public static final String ID = "about-corups";

    void setContent(String html);

    void showEditHomePage(HomePage page);
}
