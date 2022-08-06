package com.incomeCalculator.userservice.exceptions;

import com.incomeCalculator.userservice.models.User;

public class CookieNotFoundException extends RuntimeException {

    public CookieNotFoundException(User user) {
        super("Cookie not found for user with id: " + user.getId());
    }

    public CookieNotFoundException(String token) {
        super("Cookie not found for token: " + token);
    }

}
