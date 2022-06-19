package com.incomeCalculator.authapi;


import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RefreshScope
@ComponentScan(basePackages = {"com.incomeCalculator.userservice"})
public class JwtFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtTokenService service;
    @Autowired
    private RouterValidator validator;

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if(validator.isRouteOpen(request)) {
            if(isAuthMissing(request)) {
                return onError(exchange,HttpStatus.UNAUTHORIZED);
            }
            String token = service.getTokenFromRequest(request);
            if(!service.validateToken(token)) {
                return onError(exchange,HttpStatus.UNAUTHORIZED);
            }
            User user = service.getUserFromToken(token);
            exchange.getRequest().mutate()
                    .header("id",user.getId().toString())
                    .header("role",user.getRole().getRoleName())
                    .build();
            String newToken = service.generateToken(user.getLogin());
            exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION,newToken);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}

