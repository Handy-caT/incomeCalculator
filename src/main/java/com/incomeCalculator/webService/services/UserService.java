package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.RoleNotFoundException;
import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.models.UserModelAssembler;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.security.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

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
            log.info("Password: " + passwordEncoder.matches(password,user.getPassword()) + " "+ password + " " + user.getPassword() + " " + passwordEncoder.encode(password));
            if(passwordEncoder.matches(password,user.getPassword()))
                return user;
            else throw new IllegalArgumentException("Wrong password");
        }
        return null;
    }

}
