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
    @ManyToOne
    private User user;
    private String cardName;

    public Card(CurrencyUnitEntity currencyUnit, BigDecimal balance, User user, String cardName) {
        this.currencyUnit = currencyUnit;
        this.balance = balance;
        this.user = user;
        this.cardName = cardName;
    }

    public Card(Long id, CurrencyUnitEntity currencyUnit, BigDecimal balance, User user, String cardName) {
        this.id = id;
        this.currencyUnit = currencyUnit;
        this.balance = balance;
        this.user = user;
        this.cardName = cardName;
    }

    public Card() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public Money getBalance() {
        return null;
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnit() {
        return null;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void addMoneyToBalance(Money money) {
        this.balance = balance.add(money.getAmount());
    }

    @Override
    public void receiveTransaction(Transaction transaction) {
        transaction.execute(this);
    }

    @Override
    public void subtractMoneyFromBalance(Money money) {
        this.balance = balance.subtract(money.getAmount());
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", currencyUnit=" + currencyUnit +
                ", balance=" + balance +
                '}';
    }
}
