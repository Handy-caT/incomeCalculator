package com.incomeCalculator.webService.exceptions;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(Long id) {
        super("Can't find transaction with id " + id);
    }

}
