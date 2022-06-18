package com.incomeCalculator.authapi.services;

import com.incomeCalculator.authapi.exceptions.TokenNotFoundException;
import com.incomeCalculator.authapi.models.Token;
import com.incomeCalculator.authapi.models.User;
import com.incomeCalculator.authapi.repositories.TokenRepository;
import com.incomeCalculator.authapi.repositories.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtTokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
    @Autowired
    private TokenRepository repository;
    @Autowired
    private UserRepository userRepository;

    public String getTokenFromRequest(ServerHttpRequest request) {
        String bearer = request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public String getUsersToken(User user) {
        Token tokenEntity = repository.findByUser(user)
                .orElseThrow(() -> new TokenNotFoundException(user));
        return tokenEntity.getToken();
    }

    public User getUserFromToken(String token) {
        Token tokenEntity = repository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));

        return tokenEntity.getUser();
    }


    public boolean validateUsersToken(User user, String token) {
        return  Objects.equals(token, getUsersToken(user)) ||
                getUserFromToken(token).getRole().getRoleName().equals(UserService.adminRole);
    }

    public void saveToken(String token, User user) {
        Token tokenEntity = repository.findByUser(user).orElse(new Token(user, token));
        tokenEntity.setToken(token);
        repository.save(tokenEntity);
        user.setToken(tokenEntity);
        userRepository.save(user);
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
        } catch (TokenNotFoundException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.info("invalid token");
        }
        return false;
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

}
