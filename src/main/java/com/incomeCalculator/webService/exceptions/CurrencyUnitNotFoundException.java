package com.incomeCalculator.webService.exceptions;

public class CurrencyUnitNotFoundException extends RuntimeException {

    public CurrencyUnitNotFoundException(Long id) {
        super("Could not find currency unit " + id);
    }

}
