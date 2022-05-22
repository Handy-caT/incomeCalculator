package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.RoleNotFoundException;
import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class UserService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private final String userRole = "ROLE_USER";


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
            if(passwordEncoder.matches(password, user.getPassword()))
                return user;
        }
        return null;
    }

}
