package com.incomeCalculator.cardservice.controllers;


import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.modelAssemblers.CurrencyUnitModelAssembler;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.services.CurrencyUnitService;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CurrencyUnitController {

    private static final Logger log = LoggerFactory.getLogger(CurrencyUnitController.class);

    private final CurrencyUnitRepository repository;
    private final CurrencyUnitModelAssembler assembler;
    private final CurrencyUnitService service;


    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserService userService;

    CurrencyUnitController(CurrencyUnitRepository repository,
                           CurrencyUnitModelAssembler assembler,
                           CurrencyUnitService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }


    @GetMapping("/currencyUnits")
    public CollectionModel<EntityModel<CurrencyUnitEntity>> all(){

        List<EntityModel<CurrencyUnitEntity>> currencyUnits = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(currencyUnits,linkTo(methodOn(CurrencyUnitController.class).all())
                .withSelfRel());
    }

    @GetMapping("/currencyUnits/{param}")
    public EntityModel<CurrencyUnitEntity> one(@PathVariable String param
            , @RequestParam(defaultValue = "0",name = "parammode") String paramMode) {
        CurrencyUnitEntity currencyUnit = service.getCurrencyUnitWithParam(param,Long.parseLong(paramMode));
        return assembler.toModel(currencyUnit);
    }

    @PostMapping("/currencyUnits")
    public EntityModel<CurrencyUnitEntity> createCurrencyUnit(@RequestBody CurrencyUnitEntity currencyUnit,
                                                              HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            if (currencyUnit.getCurrencyName().length() > 3) {
                throw new IllegalArgumentException("Currency name must be 3 chars length");
            }
            if (!repository.findByCurrencyId(currencyUnit.getCurrencyId()).isPresent()) {
                currencyUnit = repository.save(currencyUnit);
                log.info("Currency unit saved: " + currencyUnit);
                return assembler.toModel(currencyUnit);
            } else {
                throw new IllegalArgumentException("Currency with id " + currencyUnit.getCurrencyId() +
                        " already exists");
            }
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/currencyUnits/{id}")
    public String deleteCurrencyUnit(@PathVariable Long id,
                                     HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            CurrencyUnitEntity currencyUnit = repository.findById(id)
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(id));
            repository.delete(currencyUnit);
            log.info("Currency unit deleted: " + currencyUnit);
            return "CurrencyUnit " + currencyUnit.getCurrencyName() + " has been deleted";
        } else {
            throw new PermissionException();
        }
    }

    @PutMapping("/currencyUnits/{id}")
    public EntityModel<CurrencyUnitEntity> updateCurrencyUnit(@PathVariable Long id,
                                                              @RequestBody CurrencyUnitEntity currencyUnit,
                                                              HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            currencyUnit.setId(id);
            currencyUnit = repository.save(currencyUnit);

            log.info("Currency unit updated: " + currencyUnit);
            return assembler.toModel(currencyUnit);
        } else {
            throw new PermissionException();
        }
    }

}
