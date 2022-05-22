package com.incomeCalculator.webService.security;


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
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION = "Authorization";

    private final TokenRepository tokenRepository;
    private final JwtTokenService service;
    private final CustomUserDetailsService customUserDetailsService;


    public JwtFilter(TokenRepository tokenRepository,
                     JwtTokenService service,
                     CustomUserDetailsService customUserDetailsService) {
        this.tokenRepository = tokenRepository;
        this.service = service;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && service.validateToken(token)) {
            String username = service.getUserFromToken(token).getLogin();
            CustomUserDetails customUserDetails= customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(customUserDetails,
                            null,customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
