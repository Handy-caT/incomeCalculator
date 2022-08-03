package com.incomeCalculator.userservice.modelAssemblers;

import com.incomeCalculator.userservice.models.Request;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class RequestModelAssembler
        implements RepresentationModelAssembler<Request, EntityModel<Request>> {

    @Override
    public EntityModel<Request> toModel(Request entity) {
        return null;
    }

}
