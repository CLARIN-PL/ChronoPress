package pl.clarin.chronopress.presentation.page.admin.stoplist;

import com.vaadin.cdi.UIScoped;
import javax.inject.Inject;
import pl.clarin.chronopress.business.stoplist.boundary.StopListFacade;
import pl.clarin.chronopress.business.stoplist.entity.StopList;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class StopListViewPresenter extends AbstractPresenter<StopListView> {

    @Inject
    StopListFacade facade;

    public StopList save(StopList item) {
        return  facade.save(item);
    }

    public void delete(Long id) {
        facade.delete(id);
    }

    @Override
    protected void onViewEnter() {
        getView().setStopList(facade.findAll());
    }

}