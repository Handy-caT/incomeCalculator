package com.incomeCalculator.webService.modelAssembelrs;

import com.incomeCalculator.webService.controllers.TransactionController;
import com.incomeCalculator.webService.models.TransactionEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionModelAssembler
        implements RepresentationModelAssembler<TransactionEntity, EntityModel<TransactionEntity>> {

    @Override
    public EntityModel<TransactionEntity> toModel(TransactionEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TransactionController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(TransactionController.class).all()).withRel("cards"));
    }
}