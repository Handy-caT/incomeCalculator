package com.incomeCalculator.userapi.controllers;

import com.incomeCalculator.userapi.exceptions.RequestStatisticsNotFoundException;
import com.incomeCalculator.userapi.modelAssemblers.RequestStatisticsModelAssembler;
import com.incomeCalculator.userapi.models.RequestStatistics;
import com.incomeCalculator.userapi.repositories.RequestStatisticsRepository;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.RequestDestinationRepository;
import com.incomeCalculator.userservice.repositories.RequestRepository;
import com.incomeCalculator.userservice.services.RandomString;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class RequestStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(RequestStatisticsController.class);

    private final RequestStatisticsRepository requestStatisticsRepository;
    private final RequestStatisticsModelAssembler requestModelAssembler;

    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestDestinationRepository requestDestinationRepository;

    public RequestStatisticsController(RequestStatisticsRepository requestStatisticsRepository, RequestStatisticsModelAssembler requestModelAssembler) {
        this.requestStatisticsRepository = requestStatisticsRepository;
        this.requestModelAssembler = requestModelAssembler;
    }

    @GetMapping("/statistics/requests")
    public CollectionModel<EntityModel<RequestStatistics>> all(HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if(userService.isSite(user) || userService.isAdmin(user)) {
            List<EntityModel<RequestStatistics>> requests = requestStatisticsRepository.findAll().stream()
                    .map(requestModelAssembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(requests, linkTo(RequestStatisticsController.class).withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/statistics/requests/last")
    public EntityModel<RequestStatistics> getLast(HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if(userService.isSite(user) || userService.isAdmin(user)) {
            return requestModelAssembler.toModel(requestStatisticsRepository.findFirstByOrderByCreateDateTimeDesc());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/statitics/requests/{id}")
    public EntityModel<RequestStatistics> one(@PathVariable Long id, HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if (userService.isSite(user) || userService.isAdmin(user)) {
            RequestStatistics statistics = requestStatisticsRepository.findById(id)
                    .orElseThrow(() -> new RequestStatisticsNotFoundException(id));
            return requestModelAssembler.toModel(statistics);
        } else {
            throw new PermissionException();
        }
    }

}
