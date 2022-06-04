package com.incomeCalculator.webService.security;

import com.incomeCalculator.webService.exceptions.TokenNotFoundException;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.services.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static io.jsonwebtoken.lang.Strings.hasText;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtTokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
    private final TokenRepository repository;
    private final UserRepository userRepository;


    JwtTokenService(TokenRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
        user.setToken(tokenEntity);
        userRepository.save(user);
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
        } catch (TokenNotFoundException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.info("invalid token");
        }
        return false;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public String getTokenFromResponse(HttpServletResponse response) {
        String bearer = response.getHeader(AUTHORIZATION);
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

    public boolean validateUsersToken(User user, String token) {
        return  Objects.equals(token, getUsersToken(user)) ||
                getUserFromToken(token).getRole().getRoleName().equals(UserService.adminRole);
    }

}
