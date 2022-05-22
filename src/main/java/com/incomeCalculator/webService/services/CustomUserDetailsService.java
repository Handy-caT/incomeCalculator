package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {


    private UserService service;

    public void setService(UserService service) {
        this.service = service;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.findByLogin(username);

        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
