package com.incomeCalculator.cardservice.modelAssemblers;

import com.incomeCalculator.cardservice.controllers.CardController;
import com.incomeCalculator.cardservice.models.Card;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CardModelAssembler
        implements RepresentationModelAssembler<Card, EntityModel<Card>> {

    @Override
    public EntityModel<Card> toModel(Card entity) {
        return EntityModel.of(entity,
                linkTo(CardController.class).slash(entity.getId()).withSelfRel(),
                linkTo(CardController.class).slash(entity.getId())
                        .slash("transactions").withRel("transactions"),
                linkTo((CardController.class)).withRel("cards"));
    }

}
