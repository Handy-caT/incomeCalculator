package com.incomeCalculator.webService.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Repository
public class JwtTokenRepository implements CsrfTokenRepository {

    private String secret;

    public JwtTokenRepository() {
        this.secret = "springrest";
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {

        String id = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        Date exp = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());

        String token = "";
        token = Jwts.builder()
                .setId(id)
                .setIssuedAt(date)
                .setNotBefore(date)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();


        return new DefaultCsrfToken("x-csrf-token", "_csrf", token);
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return null;
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {

    }
}
