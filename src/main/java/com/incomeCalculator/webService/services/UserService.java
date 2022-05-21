package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        if(Objects.nonNull(user)) {
            if(passwordEncoder.matches(password, user.getPassword()))
                return user;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return new org.springframework.security.core.userdetails
                .User(user.getLogin(),user.getPassword(), true, true,
                true, true, new HashSet<>());
    }

}
