package com.incomeCalculator.webService.security;

import com.incomeCalculator.webService.exceptions.TokenNotFoundException;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.TokenRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;

@Component
public class JwtTokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
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

    public User getUserFromToken(String token) {
        Token tokenEntity = repository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));
        return tokenEntity.getUser();
    }

    public boolean validateToken(String token) {
        try {
            Token tokenEntity = repository.findByToken(token)
                    .orElseThrow(() -> new TokenNotFoundException(token));
            Jwts.parser().setSigningKey(tokenEntity.getUser().getLogin()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.info("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.info("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.info("Malformed jwt");
        } catch (SignatureException sEx) {
            log.info("Invalid signature");
        } catch (Exception e) {
            log.info("invalid token");
        }
        return false;
    }

}
