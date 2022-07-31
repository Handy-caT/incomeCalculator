package com.incomeCalculator.userapi.modelAssemblers;

import com.incomeCalculator.userapi.controllers.RequestStatisticsController;
import com.incomeCalculator.userapi.models.RequestStatistics;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class RequestStatisticsModelAssembler
        implements RepresentationModelAssembler<RequestStatistics, EntityModel<RequestStatistics>> {

    @Override
    public EntityModel<RequestStatistics> toModel(RequestStatistics entity) {
        return EntityModel.of(entity,
                linkTo(RequestStatisticsController.class).slash(entity.getId()).withSelfRel(),
                linkTo((RequestStatisticsController.class)).withRel("requestStatistics"));
    }

}
