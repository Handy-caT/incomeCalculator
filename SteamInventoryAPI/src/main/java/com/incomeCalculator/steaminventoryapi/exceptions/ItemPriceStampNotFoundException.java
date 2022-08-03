package com.incomeCalculator.steaminventoryapi.exceptions;

public class ItemPriceStampNotFoundException extends RuntimeException {

    public ItemPriceStampNotFoundException(Long id) {
        super("ItemPriceStamp with id " + id + " not found");
    }

}
