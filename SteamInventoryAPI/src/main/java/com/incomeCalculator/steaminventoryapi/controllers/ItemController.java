package com.incomeCalculator.steaminventoryapi.controllers;

import com.incomeCalculator.steaminventoryapi.exceptions.ItemNotFoundException;
import com.incomeCalculator.steaminventoryapi.modelAssemblers.ItemModelAssembler;
import com.incomeCalculator.steaminventoryapi.models.Item;
import com.incomeCalculator.steaminventoryapi.repositories.ItemRepository;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.UserRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemRepository repository;
    private final ItemModelAssembler assembler;

    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public ItemController(ItemRepository repository, ItemModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/items")
    public CollectionModel<EntityModel<Item>> all(HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if(userService.isAdmin(user)) {
            List<EntityModel<Item>> items = repository.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(items,linkTo(ItemController.class)
                    .withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/items/{id}")
    public EntityModel<Item> one(HttpServletRequest request, @PathVariable Long id) {
        User user = handler.getUserFromRequest(request);
        if(userService.isAdmin(user)) {
            Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
            return assembler.toModel(item);
        } else {
            throw new PermissionException();
        }
    }


}
