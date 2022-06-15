package com.incomeCalculator.webService.exceptions;

import com.incomeCalculator.webService.models.User;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(Long id) {
        super("Can't find card with id " + id);
    }
    public CardNotFoundException(String name) {
        super("Can't find card with name " + name);
    }
    public CardNotFoundException(User user) {
        super("Can't find card for user " + user.getLogin());
    }


}
