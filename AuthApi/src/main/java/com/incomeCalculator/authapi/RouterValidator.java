package com.incomeCalculator.authapi;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RouterValidator {
    List<String> openRoutes;
    {
        openRoutes = new LinkedList<>();
        openRoutes.add("/auth");
        openRoutes.add("/register");
        openRoutes.add("/currencyUnits");
        openRoutes.add("/ratios");
    }

    public boolean isRouteOpen(ServerHttpRequest request) {
        return openRoutes.stream()
                .noneMatch(uri ->  request.getURI().getPath().contains(uri));
    }

}
