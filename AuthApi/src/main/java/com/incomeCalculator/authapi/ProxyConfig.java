package com.incomeCalculator.authapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProxyConfig {

    @Value("${CALCULATOR_HOST:localhost}")
    private String calculatorHost;

    @Value("${CALCULATOR_PORT:8081}")
    private Long calculatorPort;



    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/cards/*")
                        .filters(f -> f.stripPrefix(1))
                        .uri("https://" + calculatorHost + ':' + calculatorPort))
                .build();
    }

}
