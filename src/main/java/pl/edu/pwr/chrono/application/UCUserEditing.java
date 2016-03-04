package pl.edu.pwr.chrono.application;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.domain.User;

import java.util.List;

@Service
public interface UCUserEditing {

    User save(User user);

    List<User> list();

    void changePassword(Long userId, String password);
}
