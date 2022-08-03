package com.incomeCalculator.userapi;


import com.incomeCalculator.userapi.controllers.UserController;
import com.incomeCalculator.userservice.requests.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserModelAssembler
        implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        return EntityModel.of(entity,
                linkTo(UserController.class).slash(entity.getId()).withSelfRel(),
                linkTo(UserController.class).withRel("users"));

    }
}
