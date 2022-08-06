package com.incomeCalculator.authapi;


import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.CookieService;
import com.incomeCalculator.userservice.services.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private RouterValidator validator;

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private boolean isCookiesMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.COOKIE);
    }

    private Mono<Void> filterToken(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = jwtTokenService.getTokenFromRequest(request);
        if(!jwtTokenService.validateToken(token)) {
            return onError(exchange,HttpStatus.UNAUTHORIZED);
        }
        User user = jwtTokenService.getUserFromToken(token);
        exchange.getRequest().mutate()
                .header("id",user.getId().toString())
                .header("role",user.getRole().getRoleName())
                .build();
        String newToken = jwtTokenService.generateToken(user.getLogin());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    jwtTokenService.saveToken(newToken,user);
                    exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION,newToken);
                }
        ));
    }

    private Mono<Void> filterCookie(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String cookie = cookieService.getCookieFromRequest(request);
        if(!cookieService.validateCookie(cookie)) {
            return onError(exchange,HttpStatus.UNAUTHORIZED);
        }
        User user = cookieService.getUserFromToken(cookie);
        exchange.getRequest().mutate()
                .header("id",user.getId().toString())
                .header("role",user.getRole().getRoleName())
                .build();
        return chain.filter(exchange);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if(validator.isRouteOpen(request)) {
            if(isAuthMissing(request)) {
                if(isCookiesMissing(request)) {
                    return onError(exchange,HttpStatus.UNAUTHORIZED);
                }
                return filterCookie(exchange,chain);
            }
           return filterToken(exchange,chain);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}

