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

    @Value("${CALCULATOR_HOST:localhost}")
    private String calculatorHost;

    @Value("${CALCULATOR_PORT:8081}")
    private Long calculatorPort;

    @Autowired
    JwtFilter filter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(p -> p
                        .path("/cards/**")
                        .filters(f -> f.filter(filter))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/currencyUnits/**")
                        .filters(f -> f.filter(filter))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/ratios/**")
                        .filters(f -> f.filter(filter))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/register")
                        .filters(f -> f.filter(filter))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .route(p -> p
                        .path("/auth")
                        .filters(f -> f.filter(filter))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .build();
    }

}
