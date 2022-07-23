package com.incomeCalculator.steaminventoryapi.controllers;

import com.incomeCalculator.steaminventoryapi.exceptions.ItemPriceStampNotFoundException;
import com.incomeCalculator.steaminventoryapi.modelAssemblers.ItemPriceStampModelAssembler;
import com.incomeCalculator.steaminventoryapi.models.Item;
import com.incomeCalculator.steaminventoryapi.models.ItemPriceStamp;
import com.incomeCalculator.steaminventoryapi.repositories.ItemPriceStampRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ItemPriceStampController {

    private static final Logger log = LoggerFactory.getLogger(ItemPriceStampController.class);

    private final ItemPriceStampRepository repository;
    private final ItemPriceStampModelAssembler assembler;

    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public ItemPriceStampController(ItemPriceStampRepository repository, ItemPriceStampModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/priceStamps")
    public CollectionModel<EntityModel<ItemPriceStamp>> all(HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if(userService.isAdmin(user)) {
            List<EntityModel<ItemPriceStamp>> itemPriceStamps = repository.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(itemPriceStamps,linkTo(ItemPriceStampController.class)
                    .withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/priceStamps/{id}")
    public EntityModel<ItemPriceStamp> one(@PathVariable Long id, HttpServletRequest request) {
        User user = handler.getUserFromRequest(request);
        if(userService.isAdmin(user)) {
            ItemPriceStamp itemPriceStamp = repository.findById(id)
                    .orElseThrow(() -> new ItemPriceStampNotFoundException(id));

            return assembler.toModel(itemPriceStamp);
        } else {
            throw new PermissionException();
        }
    }

}
