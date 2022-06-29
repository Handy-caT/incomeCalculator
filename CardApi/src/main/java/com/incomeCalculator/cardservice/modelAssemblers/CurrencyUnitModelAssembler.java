package com.incomeCalculator.cardservice.modelAssemblers;

import com.incomeCalculator.cardservice.controllers.CurrencyUnitController;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
