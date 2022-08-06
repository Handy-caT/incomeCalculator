package com.incomeCalculator.userservice.services;

import com.incomeCalculator.userservice.exceptions.CookieNotFoundException;
import com.incomeCalculator.userservice.models.Cookie;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.CookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CookieService {

    @Autowired
    CookieRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Cookie createRawCookie(User user) {
        Cookie cookie = new Cookie(user);
        cookie.setToken(RandomString.generate(12));
        return cookie;
    }

    public Cookie saveCookie(Cookie cookie) {
        String token_hash = passwordEncoder.encode(cookie.getToken());
        cookie.setToken(token_hash);
        return repository.save(cookie);
    }

    public boolean validateCookie(User user, String token) {
        Cookie cookie = repository.findByUser(user).orElseThrow(() -> new CookieNotFoundException(user));
        if(cookie == null) return false;
        String token_hash = passwordEncoder.encode(token);
        return cookie.getToken().equals(token_hash);
    }

}
