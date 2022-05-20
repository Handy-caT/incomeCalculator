package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.models.UserModelAssembler;
import com.incomeCalculator.webService.repositories.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository repository;

    private final UserModelAssembler assembler;


    public UserController(UserRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all(){
        return null;
    }

    @GetMapping("/users/{id}")
    public EntityModel<Ratio> one(@PathVariable Long id) {
        return null;
    }

}
