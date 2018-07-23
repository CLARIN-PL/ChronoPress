package pl.clarin.chronopress.presentation.page.login;

import com.vaadin.cdi.CDIView;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(LoginView.ID)
public class LoginViewImpl extends AbstractView<LoginViewPresenter> implements LoginView {

    private final VerticalLayout layout = new VerticalLayout();

    @Inject
    private Instance<LoginViewPresenter> presenter;

    @PostConstruct
    public void init() {
        setCompositionRoot(layout);
        setSizeFull();
    }

    @Override
    protected LoginViewPresenter generatePresenter() {
        return presenter.get();
    }

}
