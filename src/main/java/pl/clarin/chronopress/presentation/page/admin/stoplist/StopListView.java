package pl.clarin.chronopress.presentation.page.admin.stoplist;

import java.util.List;
import pl.clarin.chronopress.business.stoplist.entity.StopList;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

public interface StopListView extends ApplicationView<StopListViewPresenter> {

    public static final String ID = "stop-list";

    void setStopList(List<StopList> list);
}
