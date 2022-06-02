package com.incomeCalculator.webService.requests;

import java.math.BigDecimal;

public class TransactionRequest {

    private String currencyName;
    private BigDecimal amount;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "currencyName='" + currencyName + '\'' +
                ", amount=" + amount +
                '}';
    }
    
}
