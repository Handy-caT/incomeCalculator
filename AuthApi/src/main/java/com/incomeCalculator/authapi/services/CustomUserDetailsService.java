package com.incomeCalculator.authapi.services;


import com.incomeCalculator.authapi.exceptions.UserNotFoundException;
import com.incomeCalculator.authapi.models.User;
import com.incomeCalculator.authapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository repository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
