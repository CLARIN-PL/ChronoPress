/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.shered.mvp;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import javax.annotation.PostConstruct;

public abstract class AbstractView<P extends AbstractPresenter> extends
        CustomComponent implements ApplicationView<P>, View {

    private static final long serialVersionUID = -1501252090846963713L;

    private P presenter;

    public AbstractView() {
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.onViewEnter();
    }

    protected void setPresenter(P presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @PostConstruct
    protected void postConstruct() {
        setPresenter(generatePresenter());
    }

    protected abstract P generatePresenter();

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    public String getName() {
            return getClass().getSimpleName();
    }

    @Override
    public String getId() {
        // Use view id/address as the id by default
        CDIView annotation = getClass().getAnnotation(CDIView.class);
        if (annotation != null) {
            return annotation.value();
        }
        return super.getId();
    }

}
