package com.incomeCalculator.userservice.controllers;

import com.incomeCalculator.userservice.RequestHandler;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.models.UserModelAssembler;
import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.requests.UserUpdateRequest;
import com.incomeCalculator.userservice.services.JwtTokenService;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;
    private final UserService service;

    private final UserModelAssembler assembler;
    @Autowired
    private RequestHandler handler;


    public UserController(UserRepository repository, UserService service, UserModelAssembler assembler) {
        this.repository = repository;
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all(HttpServletRequest request){
        User user = handler.getUserFromRequest(request);
        if(service.isAdmin(user)) {
            List<EntityModel<User>> users = repository.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(users, linkTo(UserController.class).withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getOne(@PathVariable Long id,HttpServletRequest request) {
        User userFromRequest = handler.getUserFromRequest(request);
        if(service.isAdmin(userFromRequest) || Objects.equals(userFromRequest.getId(), id)) {
            User user = repository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            return assembler.toModel(user);
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User userFromRequest = handler.getUserFromRequest(request);
        if(userFromRequest.getId().equals(id)) {
            repository.deleteById(id);
            log.info("User deleted: " + user);
        } else{
            throw new PermissionException();
        }
        return "Your account has been deleted. Goodbye!"; 
    }

    @PatchMapping("/users/{id}")
    public EntityModel<User> updateUsersPassword(@PathVariable Long id,
                                                 @RequestBody UserUpdateRequest updateRequest, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User userFromRequest = handler.getUserFromRequest(request);
        if(userFromRequest.getId().equals(id)) {
            User requestUser = service.findByLoginAndPassword(updateRequest.getLogin(),updateRequest.getOldPassword());
            if(requestUser.equals(user)) {
                user.setPassword(updateRequest.getNewPassword());
                user = service.saveUser(user);
            }
        } else{
            throw new PermissionException();
        }
        return assembler.toModel(user);
    }
}
