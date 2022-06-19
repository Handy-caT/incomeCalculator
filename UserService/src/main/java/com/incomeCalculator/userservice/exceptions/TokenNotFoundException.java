package com.incomeCalculator.userservice.exceptions;


import com.incomeCalculator.userservice.models.User;

public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(String token) {
        super("Can't find token " + token);
    }
    public TokenNotFoundException(User user) {
        super("Can't find token for user " + user.getLogin());
    }

}
