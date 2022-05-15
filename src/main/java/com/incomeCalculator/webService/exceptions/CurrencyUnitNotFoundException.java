package com.incomeCalculator.webService.exceptions;

public class CurrencyUnitNotFoundException extends RuntimeException {

    public CurrencyUnitNotFoundException(long currencyId) {
        super("Could not find currency unit " + currencyId);
    }
    public CurrencyUnitNotFoundException(String currencyName) {
        super("Could not find currency unit " + currencyName);
    }

}
