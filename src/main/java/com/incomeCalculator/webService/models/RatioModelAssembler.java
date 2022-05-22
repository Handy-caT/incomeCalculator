package com.incomeCalculator.webService.models;

import com.incomeCalculator.webService.controllers.CurrencyUnitController;
import com.incomeCalculator.webService.controllers.RatioController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RatioModelAssembler
        implements RepresentationModelAssembler<Ratio, EntityModel<Ratio>> {

    @Override
    public EntityModel<Ratio> toModel(Ratio entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(RatioController.class).one(entity.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(RatioController.class).all()).withRel("ratios"),
                linkTo(methodOn(CurrencyUnitController.class)
                        .one(entity.getCurrencyUnit().getId().toString(),"0")).withRel("currencyUnit"));
    }
}
