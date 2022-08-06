package com.incomeCalculator.userservice.services;

import com.incomeCalculator.userservice.exceptions.CookieNotFoundException;
import com.incomeCalculator.userservice.exceptions.TokenNotFoundException;
import com.incomeCalculator.userservice.models.Cookie;
import com.incomeCalculator.userservice.models.Token;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.CookieRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

@Component
public class CookieService {

    private static final Logger log = LoggerFactory.getLogger(CookieService.class);

    @Autowired
    CookieRepository repository;

    public String generateCookie(User user) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        Date exp = Date.from(LocalDateTime.now().plusYears(100).atZone(ZoneId.systemDefault()).toInstant());

        String token;
        token = Jwts.builder()
                .setId(id)
                .setIssuedAt(date)
                .setNotBefore(date)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, user.getLogin())
                .compact();

        return token;
    }

    public boolean validateCookie(String cookie) {
        try {
            Cookie cookieEntity = repository.findByToken(cookie)
                    .orElseThrow(() -> new TokenNotFoundException(cookie));
            Jwts.parser().setSigningKey(cookieEntity.getUser().getLogin()).parseClaimsJws(cookie);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.info("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.info("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.info("Malformed jwt");
        } catch (SignatureException sEx) {
            log.info("Invalid signature");
        } catch (TokenNotFoundException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.info("invalid token");
        }
        return false;
    }

    public void saveCookie(String token, User user) {
        Cookie cookie = repository.findByUser(user).orElse(new Cookie(user));
        cookie.setToken(token);
        repository.save(cookie);
    }

    public Cookie getCookieByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new CookieNotFoundException(user));
    }

    public User getUserFromToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new CookieNotFoundException(token)).getUser();
    }

    public String getCookieFromRequest(ServerHttpRequest request) {
        String cookie = request.getHeaders().getOrEmpty(HttpHeaders.COOKIE).get(0);
        if (hasText(cookie)) {
            return cookie;
        }
        return null;
    }


}
