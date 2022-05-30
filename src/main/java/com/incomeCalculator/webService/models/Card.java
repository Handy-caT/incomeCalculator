package com.incomeCalculator.webService.models;

import com.incomeCalculator.core.wallet.card.CardProvider;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "CARDS")
public class Card implements CardProvider {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private CurrencyUnitEntity currencyUnit;
    private BigDecimal balance;

    public Card(CurrencyUnitEntity currencyUnit, BigDecimal balance) {
        this.currencyUnit = currencyUnit;
        this.balance = balance;
    }

    public Card() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Money getBalance() {
        return null;
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnit() {
        return null;
    }

    @Override
    public void addMoneyToBalance(Money money) {

    }

    @Override
    public void receiveTransaction(Transaction transaction) {

    }

    @Override
    public void subtractMoneyFromBalance(Money money) {

    }
}
