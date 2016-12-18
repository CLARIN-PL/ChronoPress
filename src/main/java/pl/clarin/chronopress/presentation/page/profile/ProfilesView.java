package pl.clarin.chronopress.presentation.page.profile;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface ProfilesView extends ApplicationView<ProfilesViewPresenter> {

    public static final String ID = "profile";

    public void addResultPanel(CalculationResult result);
}
