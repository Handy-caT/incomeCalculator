package com.incomeCalculator.webService.controllers;


import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserRepository repository;

    AuthController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/auth/login")
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        User user;
        if(principal instanceof User) {
            user = (User) principal;
        } else return null;
        return repository.findByLogin(user.getLogin())
                .orElseThrow(() -> new UserNotFoundException(user.getLogin()));
    }
}
