package com.incomeCalculator.userservice.services;


import com.incomeCalculator.userservice.exceptions.RoleNotFoundException;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.user.RoleRepository;
import com.incomeCalculator.userservice.repositories.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    public static final String userRole = "ROLE_USER";
    public static final String adminRole = "ROLE_ADMIN";
    public static final String siteRole = "ROLE_SITE";


    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByRoleName(userRole)
                .orElseThrow(() -> new RoleNotFoundException(userRole)));

        return repository.save(user);
    }

    public User findByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        if(Objects.nonNull(user)) {
            if(passwordEncoder.matches(password,user.getPassword()))
                return user;
            else throw new IllegalArgumentException("Wrong password");
        }
        return null;
    }

    public boolean isSite(User user) {
        return user.getRole().getRoleName().equals(siteRole);
    }

    public boolean isAdmin(User user) {
        return user.getRole().getRoleName().equals(adminRole);
    }

    public boolean validateUser(Long usersId,User requestUser) {
        return isAdmin(requestUser) || requestUser.getId().equals(usersId);
    }

    public User makeAdmin(User user) {
        user.setRole(roleRepository.findByRoleName(adminRole)
                .orElseThrow(() -> new RoleNotFoundException(adminRole)));
        return repository.save(user);
    }

    public User makeUser(User user) {
        user.setRole(roleRepository.findByRoleName(userRole)
                .orElseThrow(() -> new RoleNotFoundException(userRole)));
        return repository.save(user);
    }

}
