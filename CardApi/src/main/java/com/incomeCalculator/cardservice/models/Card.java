package com.incomeCalculator.cardservice.models;

import com.incomeCalculator.core.wallet.card.CardProvider;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.money.currencyUnit.CurrencyUnit;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

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
        return Money.of(currencyUnit,balance);
    }

    @Override
    public CurrencyUnit getCurrencyUnit() {
        return currencyUnit;
    }


    public String getUserName() {
        return user.getLogin();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(currencyUnit, card.currencyUnit)
                && Objects.equals(balance, card.balance) && Objects.equals(user, card.user)
                && Objects.equals(cardName, card.cardName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyUnit, balance, user, cardName);
    }

}
