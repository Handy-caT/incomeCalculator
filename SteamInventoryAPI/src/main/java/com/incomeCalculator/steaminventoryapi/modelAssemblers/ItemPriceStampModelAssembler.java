package com.incomeCalculator.steaminventoryapi.modelAssemblers;

import com.incomeCalculator.steaminventoryapi.controllers.ItemController;
import com.incomeCalculator.steaminventoryapi.models.ItemPriceStamp;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ItemPriceStampModelAssembler implements RepresentationModelAssembler<ItemPriceStamp, EntityModel<ItemPriceStamp>> {

    @Override
    public EntityModel<ItemPriceStamp> toModel(ItemPriceStamp entity) {
        return EntityModel.of(entity,
                linkTo(ItemController.class).slash(entity.getId()).withSelfRel(),
                linkTo((ItemController.class)).withRel("priceStamps"));
    }

}


