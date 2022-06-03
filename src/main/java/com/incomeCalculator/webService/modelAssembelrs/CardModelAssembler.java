package com.incomeCalculator.webService.modelAssembelrs;

import com.incomeCalculator.webService.controllers.CardController;
import com.incomeCalculator.webService.controllers.CurrencyUnitController;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CardModelAssembler
        implements RepresentationModelAssembler<Card, EntityModel<Card>> {

    @Override
    public EntityModel<Card> toModel(Card entity) {
        return EntityModel.of(entity,
                linkTo(CardController.class).slash(entity.getId()).withSelfRel(),
                linkTo(methodOn(CardController.class).all()).withRel("cards"));
    }

}
