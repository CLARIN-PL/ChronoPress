package pl.edu.pwr.chrono.webui.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import pl.edu.pwr.chrono.webui.infrastructure.event.UIEventBus;

import javax.annotation.PreDestroy;
import java.io.Serializable;

/**
 * Created by tnaskret on 08.01.16.
 */
public abstract class Presenter<T> implements Serializable {

    protected T view;

    @Autowired
    private UIEventBus eventBus;

    public void setView(T view) {
        Assert.notNull(view);
        this.view = view;
        eventBus.register(this);
    }

    public T getView(){
        return view;
    }

    @PreDestroy
    public void destroy() {
        eventBus.unregister(this);
    }

}
