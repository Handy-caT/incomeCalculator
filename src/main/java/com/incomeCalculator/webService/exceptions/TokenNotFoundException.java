package com.incomeCalculator.webService.exceptions;

public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(String token) {
        super("Can't find token " + token);
    }

}
