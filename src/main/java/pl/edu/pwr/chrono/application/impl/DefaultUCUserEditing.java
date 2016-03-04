package pl.edu.pwr.chrono.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.application.UCUserEditing;
import pl.edu.pwr.chrono.domain.User;
import pl.edu.pwr.chrono.repository.UserRepository;

import java.util.List;

@Service
public class DefaultUCUserEditing implements UCUserEditing {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
        return user;
    }

    @Override
    public List<User> list() {
        return repository.findAll();
    }

    @Override
    public void changePassword(Long userId, String password) {
        User user = repository.findOne(userId);
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }
}
