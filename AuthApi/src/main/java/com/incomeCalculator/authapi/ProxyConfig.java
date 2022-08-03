package com.incomeCalculator.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class ProxyConfig {

    @Value("${card.host}")
    private String calculatorHost;
    @Value("${card.port}")
    private Long calculatorPort;

    @Value("${user.host}")
    private String usersHost;
    @Value("${user.port}")
    private Long usersPort;

    @Autowired
    JwtFilter filter;
    @Autowired
    RequestFilter requestFilter;

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(p -> p
                        .path("/cards/**")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/currencyUnits/**")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/ratios/**")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/register")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + usersHost + ':' + usersPort))
                .route(p -> p
                        .path("/auth")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + usersHost + ':' + usersPort))
                .route(p -> p
                        .path("/users/**")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + usersHost + ':' + usersPort))
                .route(p -> p
                        .path("/statistics/**")
                        .filters(f -> f.filter(requestFilter).filter(filter))
                        .uri("http://" + usersHost + ':' + usersPort))
                .build();
    }

}
