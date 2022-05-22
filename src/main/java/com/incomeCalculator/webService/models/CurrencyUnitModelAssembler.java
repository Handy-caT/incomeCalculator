package com.incomeCalculator.webService.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.incomeCalculator.webService.controllers.CurrencyUnitController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CurrencyUnitModelAssembler
        implements RepresentationModelAssembler<CurrencyUnitEntity, EntityModel<CurrencyUnitEntity>> {

    @Override
    public EntityModel<CurrencyUnitEntity> toModel(CurrencyUnitEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CurrencyUnitController.class).one(entity.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits"));
    }
}
