package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.RoleNotFoundException;
import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    private final String userRole = "ROLE_USER";


    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByRoleName(userRole)
                .orElseThrow(() -> new RoleNotFoundException(userRole)));

        log.info("User saved: " + user);
        return repository.save(user);
    }

    public User findByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        log.info("User by login " + login + ": " + user);
        if(Objects.nonNull(user)) {
            if(passwordEncoder.matches(password,user.getPassword()))
                return user;
            else throw new IllegalArgumentException("Wrong password");
        }
        return null;
    }

}
