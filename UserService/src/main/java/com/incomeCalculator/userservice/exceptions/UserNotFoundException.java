package com.incomeCalculator.userservice.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Could not find user with id " + id);
    }

    public UserNotFoundException(String login) {
        super("Could not find user with login " + login);
    }

}
