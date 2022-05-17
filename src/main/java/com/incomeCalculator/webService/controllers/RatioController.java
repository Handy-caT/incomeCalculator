package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.exceptions.RatioNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.models.RatioModelAssembler;
import com.incomeCalculator.webService.repositories.RatioRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RatioController {

    private final RatioRepository repository;
    private final RatioModelAssembler assembler;

    public RatioController(RatioRepository repository, RatioModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/ratios")
    public CollectionModel<EntityModel<Ratio>> all(){

        List<EntityModel<Ratio>> currencyUnits = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(currencyUnits,linkTo(methodOn(RatioController.class).all())
                .withSelfRel());
    }

    @GetMapping("/ratios/{param}")
    public EntityModel<Ratio> one(@PathVariable String param
            , @RequestParam(defaultValue = "0",name = "parammode") String paramMode) {
        Ratio ratio;
        if(Objects.equals(paramMode, "1")) {
            ratio = repository.findByCurrencyUnit_CurrencyName(param)
                    .orElseThrow(() -> new RatioNotFoundException(param));
        } else {
            ratio = repository.findById(Long.parseLong(param))
                    .orElseThrow(() -> new RatioNotFoundException(Long.parseLong(param)));
        }
        return assembler.toModel(ratio);
    }

}
