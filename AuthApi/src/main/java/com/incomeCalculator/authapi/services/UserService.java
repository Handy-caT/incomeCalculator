package com.incomeCalculator.authapi.services;


import com.incomeCalculator.authapi.exceptions.RoleNotFoundException;
import com.incomeCalculator.authapi.exceptions.UserNotFoundException;
import com.incomeCalculator.authapi.models.User;
import com.incomeCalculator.authapi.repositories.RoleRepository;
import com.incomeCalculator.authapi.repositories.UserRepository;
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

    public boolean isAdmin(User user) {
        return user.getRole().getRoleName().equals(adminRole);
    }

}
