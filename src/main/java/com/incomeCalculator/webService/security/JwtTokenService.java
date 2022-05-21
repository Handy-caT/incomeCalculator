package com.incomeCalculator.webService.security;

import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;

@Repository
public class JwtTokenService {

    private final TokenRepository repository;

    JwtTokenService(TokenRepository repository) {
        this.repository = repository;
    }

    public String generateToken(String login) {

        String id = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        Date exp = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        String token;
        token = Jwts.builder()
                .setId(id)
                .setIssuedAt(date)
                .setNotBefore(date)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, login)
                .compact();

        return token;
    }

    public void saveToken(String token, User user) {
        Token tokenEntity = repository.findByUser(user).orElse(new Token(user, token));
        tokenEntity.setToken(token);
        repository.save(tokenEntity);
    }

}
