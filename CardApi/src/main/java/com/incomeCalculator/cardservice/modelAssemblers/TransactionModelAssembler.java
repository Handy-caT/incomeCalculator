package com.incomeCalculator.cardservice.modelAssemblers;

import com.incomeCalculator.cardservice.controllers.TransactionController;
import com.incomeCalculator.cardservice.models.TransactionEntity;
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
                linkTo(methodOn(TransactionController.class).all()).withRel("transactions"));
    }
}
