package com.incomeCalculator.webService.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class CurrencyUnitController {

    private final CurrencyUnitRepository repository;
    private final CurrencyUnitModelAssembler assembler;
    private final CurrencyUnitService service;

    CurrencyUnitController(CurrencyUnitRepository repository, CurrencyUnitModelAssembler assembler, CurrencyUnitService service) {
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




}
