package com.incomeCalculator.webService.models;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class TransactionModel implements Transaction {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private CurrencyUnitEntity currencyUnit;
    private BigDecimal transactionAmount;

    public TransactionModel() {

    }

    public TransactionModel(Long id, CurrencyUnitEntity currencyUnit, BigDecimal transactionAmount) {
        this.id = id;
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
    }

    public TransactionModel(CurrencyUnitEntity currencyUnit, BigDecimal transactionAmount) {
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
    }

    @Override
    public void execute(Card card) {

    }

    @Override
    public Money getTransactionAmount() {
        return Money.of(currencyUnit,transactionAmount);
    }

    @Override
    public Transaction revert() {
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyUnitEntity getCurrencyUnit() {
        return currencyUnit;
    }

}
