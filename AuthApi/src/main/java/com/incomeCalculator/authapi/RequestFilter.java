package com.incomeCalculator.authapi;

import com.incomeCalculator.userservice.models.Request;
import com.incomeCalculator.userservice.models.RequestDestination;
import com.incomeCalculator.userservice.models.RequestSource;
import com.incomeCalculator.userservice.models.Response;
import com.incomeCalculator.userservice.repositories.RequestDestinationRepository;
import com.incomeCalculator.userservice.repositories.RequestRepository;
import com.incomeCalculator.userservice.repositories.RequestSourceRepository;
import com.incomeCalculator.userservice.repositories.ResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
public class RequestFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    @Autowired
    private RequestRepository repository;
    @Autowired
    private RequestDestinationRepository destinationRepository;
    @Autowired
    private RequestSourceRepository sourceRepository;
    @Autowired
    private ResponseRepository responseRepository;

    private HashMap<String, String> requestMap = new HashMap<>();

    public RequestFilter() {
        requestMap.put("/auth", "UserAPI");
        requestMap.put("/register", "UserAPI");
        requestMap.put("/currencyUnits", "CardAPI");
        requestMap.put("/ratios", "CardAPI");
        requestMap.put("/cards","CardAPI");
        requestMap.put("/users","UserAPI");
        requestMap.put("/statistics","UserAPI");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        Request requestModel = new Request(path);
        requestModel = repository.save(requestModel);

        log.info("Request: " + requestModel);

        RequestDestination destination = new RequestDestination(requestModel);

        RequestSource source = new RequestSource(requestModel);
        if(!request.getRemoteAddress().isUnresolved()) {
            source.setSourceIp(request.getRemoteAddress().getAddress().getHostAddress());
        }
        log.info("Source: " + sourceRepository.save(source));

        for(String key : requestMap.keySet()) {
            if(path.contains(key)) {
                destination.setApiName(requestMap.get(key));
                break;
            }
        }
        destination.setRequestType(request.getMethod().name());
        log.info("Destination: " + destinationRepository.save(destination));

        Response responseModel = new Response(requestModel);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            responseModel.setStatus(exchange.getResponse().getStatusCode().value());
            responseModel.setStatusText(exchange.getResponse().getStatusCode().name());

            log.info("Response: " + responseRepository.save(responseModel));
        }));
    }
}
