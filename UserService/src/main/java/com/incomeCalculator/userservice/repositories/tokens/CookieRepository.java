package com.incomeCalculator.userservice.repositories.tokens;

import com.incomeCalculator.userservice.models.Cookie;
import com.incomeCalculator.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CookieRepository extends JpaRepository<Cookie, Long> {

    Optional<Cookie> findByUser(User user);

    Optional<Cookie> findByToken(String token);

}