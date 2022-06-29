package com.incomeCalculator.cardservice.exceptions;

public class RatioNotFoundException extends RuntimeException{

    public RatioNotFoundException(long ratioId) {
        super("Could not find ratio with id " + ratioId);
    }
    public RatioNotFoundException(String currencyName) {
        super("Could not find ratio with currency name " + currencyName);
    }

}
