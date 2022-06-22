package com.incomeCalculator.cardservice.exceptions;

import com.incomeCalculator.cardservice.models.Card;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(Long id) {
        super("Can't find transaction with id " + id);
    }

    public TransactionNotFoundException(Card card) {
        super("Can't find transaction for card " + card);
    }

}
