package com.incomeCalculator.webService.security;


import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.services.CustomUserDetails;
import com.incomeCalculator.webService.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtTokenService service;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = service.getTokenFromRequest(request);
        if (token != null) {
            if(service.validateToken(token)) {
                User user = service.getUserFromToken(token);
                String username = user.getLogin();
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(customUserDetails,
                                null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

                String newToken = service.generateToken(username);
                response.addHeader(AUTHORIZATION, "Bearer " + newToken);

                filterChain.doFilter(request, response);

                service.saveToken(newToken, user);

            } else {
                filterChain.doFilter(request, response);
            }
        }
        else {
            log.info("Token is null");
            filterChain.doFilter(request, response);
        }
    }

}
