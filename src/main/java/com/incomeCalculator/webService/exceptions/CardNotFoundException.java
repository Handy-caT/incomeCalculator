package com.incomeCalculator.webService.exceptions;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(Long id) {
        super("Can't find card with id " + id);
    }

}