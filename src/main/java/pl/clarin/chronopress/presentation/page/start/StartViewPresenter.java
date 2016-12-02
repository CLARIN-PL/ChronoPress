package pl.clarin.chronopress.presentation.page.start;

import com.vaadin.cdi.UIScoped;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataAnalyseView;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class StartViewPresenter extends AbstractPresenter<StartView> {

    @Inject
    javax.enterprise.event.Event<SearchAndShowConcordanceEvent> sasEvent;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Override
    protected void onViewEnter() {
    }

    public void onSearch(String lemma) {
        navigation.fire(new NavigationEvent(DataAnalyseView.ID));
        sasEvent.fire(new SearchAndShowConcordanceEvent(lemma));
    }
}
