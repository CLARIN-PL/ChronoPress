package pl.edu.pwr.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.domain.User;
import pl.edu.pwr.chrono.repository.UserRepository;

@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceimpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repository.findByUserName(username);

        if (user == null)
            throw new UsernameNotFoundException("Nie ma takiego użytkownika: " + username);

        ProfileAdapter profileAapter = new ProfileAdapter(user);
        return profileAapter;
    }
}