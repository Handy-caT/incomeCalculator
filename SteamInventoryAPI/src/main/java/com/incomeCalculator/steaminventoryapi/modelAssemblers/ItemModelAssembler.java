package com.incomeCalculator.steaminventoryapi.modelAssemblers;

import com.incomeCalculator.steaminventoryapi.controllers.ItemController;
import com.incomeCalculator.steaminventoryapi.models.Item;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ItemModelAssembler
        implements RepresentationModelAssembler<Item, EntityModel<Item>> {

    @Override
    public EntityModel<Item> toModel(Item entity) {
        return EntityModel.of(entity,
                linkTo(ItemController.class).slash(entity.getId()).withSelfRel(),
                linkTo((ItemController.class)).withRel("items"));
    }


}
