package com.incomeCalculator.webService.security;


import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.services.CustomUserDetails;
import com.incomeCalculator.webService.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION = "Authorization";

    private final JwtTokenService service;
    private final CustomUserDetailsService customUserDetailsService;


    public JwtFilter(JwtTokenService service,
                     CustomUserDetailsService customUserDetailsService) {
        this.service = service;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String token = service.getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null) {
            log.info("Token: " + token);
            if(service.validateToken(token)) {
                User user = service.getUserFromToken(token);
                String username = user.getLogin();
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(customUserDetails,
                                null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

                String newToken = service.generateToken(username);
                service.saveToken(newToken, user);
                log.info("New token: " + user);
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.addHeader(AUTHORIZATION,"Bearer " + newToken);
            }
        }
        else log.info("Token is null");
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
