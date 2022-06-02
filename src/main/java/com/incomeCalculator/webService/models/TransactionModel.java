package com.incomeCalculator.webService.models;

import com.incomeCalculator.core.wallet.card.CardProvider;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.webService.util.CurrencyConverterSQL;
import com.incomeCalculator.webService.util.CurrencyUpdaterSQL;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class TransactionModel implements Transaction {

    @Autowired
    @Transient
    CurrencyUpdaterSQL updater;

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private CurrencyUnitEntity currencyUnit;
    private BigDecimal transactionAmount;
    private boolean addition;
    @ManyToOne
    private Card card;

    public TransactionModel() {

    }

    public TransactionModel(Long id, CurrencyUnitEntity currencyUnit,
                            BigDecimal transactionAmount, boolean addition) {
        this.id = id;
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
        this.addition = addition;
    }

    public TransactionModel(CurrencyUnitEntity currencyUnit,
                            BigDecimal transactionAmount, boolean addition) {
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
        this.addition = addition;
    }

    @Override
    public void execute(CardProvider card) {
        CurrencyConverterSQL converter = new CurrencyConverterSQL(updater);
        Money moneyAmount = Money.of(currencyUnit,transactionAmount);
        if(!card.getCurrencyUnit().equals(currencyUnit)) {
            moneyAmount = converter.convert(moneyAmount,currencyUnit);
        }

        if(addition) {
            card.addMoneyToBalance(moneyAmount);
        } else {
            card.subtractMoneyFromBalance(moneyAmount);
        }
        this.card = (Card) card;
    }

    @Override
    public Money getTransactionAmount() {
        return Money.of(currencyUnit,transactionAmount);
    }

    @Override
    public Transaction revert() {
        return new TransactionModel(currencyUnit,transactionAmount,!addition);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public CurrencyUnitEntity getCurrencyUnit() {
        return currencyUnit;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + id +
                ", currencyUnit=" + currencyUnit +
                ", transactionAmount=" + transactionAmount +
                ", addition=" + addition +
                ", card=" + card +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionModel)) return false;
        TransactionModel that = (TransactionModel) o;
        return addition == that.addition && Objects.equals(id, that.id) &&
                Objects.equals(currencyUnit, that.currencyUnit) &&
                Objects.equals(transactionAmount, that.transactionAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyUnit, transactionAmount, addition);
    }

}
