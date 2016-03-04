package pl.edu.pwr.chrono.webui.ui.admin.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.application.UCUserEditing;
import pl.edu.pwr.chrono.domain.User;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class UserPresenter extends Presenter<UserView> {

    @Autowired
    private UCUserEditing ucUserEditing;

    public List<User> getAllUsers() {
        return ucUserEditing.list();
    }

    public void saveUser(User user) {
        ucUserEditing.save(user);
    }
}
