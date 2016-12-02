package pl.clarin.chronopress.business.user.boundary;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import pl.clarin.chronopress.business.security.BCryptPasswordService;
import pl.clarin.chronopress.business.user.entity.User;

@Stateless
@Startup
public class UserFacade {

    @Inject
    UserRepository userRepository;

    @Inject
    BCryptPasswordService passwordService;

    @PostConstruct
    public void init() {
        createAdminUserIfDoesntExist();
    }

    @Transactional
    public User save(User user) throws RuntimeException {
        if (isPersistedCustomerHavingSameUsername(user)) {
            throw new RuntimeException(
                    "Zmiana nazwy uzytkownika jest niedozwolona");
        } else {
            return userRepository.saveAndFlushAndRefresh(user);
        }
    }

    private boolean isPersistedCustomerHavingSameUsername(User user) {
        User existingUserRecord = findUserByUsername(user.getUsername());
        if (existingUserRecord == null) {
            return false;
        }
        if (existingUserRecord.getId().equals(user.getId())) {
            return false;
        }
        return user.getUsername().equals(existingUserRecord.getUsername());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void remove(User user) {
        User usr = userRepository.findBy(user.getId());
        userRepository.remove(usr);
    }

    public User findUserByUsername(String username) {
        try {
            return userRepository.findByUsernameEqual(username);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public boolean changeUserPassword(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null) {
            user.setPassword(passwordService.encryptPassword("test"));
            save(user);
            return true;
        }
        return false;
    }

    private void createAdminUserIfDoesntExist() {
        User developer = findUserByUsername("developer");
        if (developer == null) {
            developer = new User();
            developer.setRole(User.Role.MODERATOR);
            developer.setUsername("developer");
            developer.setEmail("naskret.tomasz@gmail.com");
            developer.setPassword(passwordService.encryptPassword("test"));
            save(developer);
        }
    }
}
