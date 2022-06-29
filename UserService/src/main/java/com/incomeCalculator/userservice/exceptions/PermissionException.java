package com.incomeCalculator.userservice.exceptions;

public class PermissionException extends RuntimeException{

    public PermissionException() {
        super("You don't have permission to do that");
    }

}
