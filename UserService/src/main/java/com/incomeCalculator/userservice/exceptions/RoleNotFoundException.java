package com.incomeCalculator.userservice.exceptions;

public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(String roleName) {
        super("Can't find role with name " + roleName.substring(4));
    }

}
