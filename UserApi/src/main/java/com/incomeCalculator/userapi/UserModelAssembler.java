package com.incomeCalculator.userapi;


import com.incomeCalculator.userapi.controllers.UserController;
import com.incomeCalculator.userservice.requests.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserModelAssembler
        implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto entity) {
        return EntityModel.of(entity,
                linkTo(UserController.class).slash(entity.getId()).withSelfRel(),
                linkTo(UserController.class).withRel("users"));

    }
}
