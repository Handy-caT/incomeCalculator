package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.models.Role;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService service;

    @Test
    public void findByLoginTest() {
        Long id = 1L;

        Role userRole = new Role("ROLE_USER");
        String userLogin = "login";
        User user = new User(id,userLogin,"password",userRole);

        when(repository.findByLogin(userLogin)).thenReturn(Optional.of(user));

        User foundUser = service.findByLogin(userLogin);
        assertEquals(foundUser,user);
    }

    @Test
    public void findByLoginAndPasswordTest() {
        Long id = 1L;

        Role userRole = new Role("ROLE_USER");
        String userLogin = "login";
        String password = "password";
        String passwordEncoded = passwordEncoder.encode(password);
        User user = new User(id,userLogin,passwordEncoded,userRole);

        when(repository.findByLogin(userLogin)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.matches(password,passwordEncoded)).thenReturn(true);

        User foundUser = service.findByLoginAndPassword(userLogin,password);
        assertEquals(user,foundUser);
    }

    @Test
    public void saveUserTest() {
        Long id = 1L;

        Role userRole = new Role("ROLE_USER");
        String userLogin = "login";
        String password = "password";
        String passwordEncoded = passwordEncoder.encode(password);
        User user = new User(id,userLogin,password,userRole);
        User userEncoded = new User(id,userLogin,passwordEncoded,userRole);

        when(repository.save(user)).thenReturn(userEncoded);
        when(mockPasswordEncoder.encode(password)).thenReturn(passwordEncoded);
        when(roleRepository.findByRoleName(userRole.getRoleName()))
                .thenReturn(Optional.of(userRole));

        User foundUser = service.saveUser(user);
        assertEquals(userEncoded,foundUser);
    }

}