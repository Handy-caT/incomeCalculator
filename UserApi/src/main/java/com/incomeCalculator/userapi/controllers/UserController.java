package com.incomeCalculator.userapi.controllers;

import com.incomeCalculator.userservice.requests.UserResponse;
import com.incomeCalculator.userservice.requests.UserUpdateRequest;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userapi.UserModelAssembler;
import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    public CollectionModel<EntityModel<UserResponse>> all(HttpServletRequest request){
        User user = handler.getUserFromRequest(request);
        if(service.isAdmin(user)) {
            List<EntityModel<UserResponse>> users = repository.findAll().stream().map(UserResponse::new)
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(users, linkTo(UserController.class).withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/users/{id}")
    public EntityModel<UserResponse> getOne(@PathVariable Long id,HttpServletRequest request) {
        User authUser= handler.getUserFromRequest(request);
        if(service.validateUser(id,authUser)) {
            if(service.isAdmin(authUser)){
                User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
                return assembler.toModel(new UserResponse(user));
            } else {
                return assembler.toModel(new UserResponse(authUser));
            }
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/users/me")
    public EntityModel<UserResponse> getMe(HttpServletRequest request) {
        User authUser= handler.getUserFromRequest(request);

        return assembler.toModel(new UserResponse(authUser));
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User authUser = handler.getUserFromRequest(request);
        if(service.validateUser(id,authUser)) {
            repository.deleteById(id);
            log.info("User deleted: " + user);
        } else{
            throw new PermissionException();
        }
        return "Your account has been deleted. Goodbye!"; 
    }

    @PatchMapping("/users/{id}/password")
    public EntityModel<UserResponse> updateUsersPassword(@PathVariable Long id,
                                                 @RequestBody UserUpdateRequest updateRequest, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User authUser = handler.getUserFromRequest(request);
        if(service.validateUser(id,authUser)) {
            User requestUser = service.findByLoginAndPassword(updateRequest.getLogin(),updateRequest.getOldPassword());
            if(requestUser.equals(user) || service.isAdmin(authUser)) {
                user.setPassword(updateRequest.getNewPassword());
                user = service.saveUser(user);
                log.info("User " + user.getLogin() + " updated password");
            } else {
                throw new IllegalArgumentException("Wrong password");
            }
        } else{
            throw new PermissionException();
        }
        return assembler.toModel(new UserResponse(user));
    }

    @PatchMapping("/users/{id}/makeAdmin")
    public EntityModel<UserResponse> makeUserAdmin(@PathVariable Long id, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User authUser = handler.getUserFromRequest(request);
        if(service.isAdmin(authUser)) {
            user = service.makeAdmin(user);
            log.info("User " + user.getLogin() + " made admin");
        } else{
            throw new PermissionException();
        }
        return assembler.toModel(new UserResponse(user));
    }

    @PatchMapping("/users/{id}/makeUser")
    public EntityModel<UserResponse> makeUserUser(@PathVariable Long id, HttpServletRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        User authUser = handler.getUserFromRequest(request);
        if(service.isAdmin(authUser)) {
            user = service.makeUser(user);
            log.info("User " + user.getLogin() + " made user");
        } else{
            throw new PermissionException();
        }
        return assembler.toModel(new UserResponse(user));
    }

}
