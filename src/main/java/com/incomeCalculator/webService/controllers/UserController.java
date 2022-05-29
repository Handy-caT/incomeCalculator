package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.exceptions.PermissionException;
import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.models.UserModelAssembler;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.requests.UserUpdateRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;
    private final UserService service;

    private final UserModelAssembler assembler;
    private final JwtTokenService tokenService;


    public UserController(UserRepository repository, UserService service, UserModelAssembler assembler, JwtTokenService tokenService) {
        this.repository = repository;
        this.service = service;
        this.assembler = assembler;
        this.tokenService = tokenService;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all(){

        List<EntityModel<User>> users= repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,linkTo(methodOn(UserController.class).all())
                .withSelfRel());
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getOne(@PathVariable Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletResponse response) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        String token = tokenService.getTokenFromResponse(response);
        if(Objects.equals(token, tokenService.getUsersToken(user))) {
            repository.deleteById(id);
            log.info("User deleted: " + user);
        } else{
            throw new PermissionException();
        }
        return "Your account has been deleted. Goodbye!"; 
    }

    @PutMapping("/users/{id}")
    public User replaceUser(@PathVariable Long id,
                            @RequestBody UserUpdateRequest request, HttpServletResponse response) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        String token = tokenService.getTokenFromResponse(response);
        if(Objects.equals(token, tokenService.getUsersToken(user))) {
            log.info("Request: " + request.toString());
            User requestUser = service.findByLoginAndPassword(request.getLogin(),request.getOldPassword());
            if(requestUser.equals(user)) {
                user.setPassword(request.getNewPassword());
                user = service.saveUser(user);
            }
        } else{
            throw new PermissionException();
        }
        return user;
    }
}
