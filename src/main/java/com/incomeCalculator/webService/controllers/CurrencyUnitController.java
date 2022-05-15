package com.incomeCalculator.webService.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CurrencyUnitController {

    private final CurrencyUnitRepository repository;
    private final CurrencyUnitModelAssembler assembler;

    CurrencyUnitController(CurrencyUnitRepository repository, CurrencyUnitModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    @GetMapping("/currencyUnits")
    public CollectionModel<EntityModel<CurrencyUnitEntity>> all(){

        List<EntityModel<CurrencyUnitEntity>> currencyUnits = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(currencyUnits,linkTo(methodOn(CurrencyUnitController.class).all())
                .withSelfRel());
    }

    @GetMapping("/currencyUnits/{id}")
    public EntityModel<CurrencyUnitEntity> one(@PathVariable long id) {

        CurrencyUnitEntity currencyUnit = repository.findById(id)
                .orElseThrow(() ->new CurrencyUnitNotFoundException(id));

        return assembler.toModel(currencyUnit);
    }

}
