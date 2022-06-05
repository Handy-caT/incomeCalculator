package com.incomeCalculator.webService.exceptions;

import com.incomeCalculator.webService.models.User;

public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(String token) {
        super("Can't find token " + token);
    }
    public TokenNotFoundException(User user) {
        super("Can't find token for user " + user.getLogin());
    }

}
