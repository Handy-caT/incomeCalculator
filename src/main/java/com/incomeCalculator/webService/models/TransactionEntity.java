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
public class TransactionEntity implements Transaction {


    @Transient
    private CurrencyUpdaterSQL updater;

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private CurrencyUnitEntity currencyUnit;
    private BigDecimal transactionAmount;
    private boolean addition;
    @ManyToOne
    private Card card;

    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;

    public TransactionEntity() {

    }

    public TransactionEntity(Long id, CurrencyUnitEntity currencyUnit,
                             BigDecimal transactionAmount, boolean addition) {
        this.id = id;
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
        this.addition = addition;
    }

    public TransactionEntity(CurrencyUnitEntity currencyUnit,
                             BigDecimal transactionAmount, boolean addition) {
        this.currencyUnit = currencyUnit;
        this.transactionAmount = transactionAmount;
        this.addition = addition;
    }

    @Override
    public void execute(CardProvider card) {
        CurrencyConverterSQL converter = new CurrencyConverterSQL(updater);
        Money moneyAmount = Money.of(currencyUnit,transactionAmount.abs());
        if(!card.getCurrencyUnit().equals(currencyUnit)) {
            moneyAmount = converter.convert(moneyAmount,card.getCurrencyUnit());
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
        return new TransactionEntity(currencyUnit,transactionAmount,!addition);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return card.getCardName();
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public CurrencyUnitEntity getCurrencyUnit() {
        return currencyUnit;
    }

    public BigDecimal getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public BigDecimal getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + id +
                ", currencyUnit=" + currencyUnit +
                ", transactionAmount=" + transactionAmount +
                ", addition=" + addition +
                ", card=" + card.getCardName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionEntity)) return false;
        TransactionEntity that = (TransactionEntity) o;
        return addition == that.addition && Objects.equals(id, that.id) &&
                Objects.equals(currencyUnit, that.currencyUnit) &&
                Objects.equals(transactionAmount, that.transactionAmount);
    }

    public void setUpdater(CurrencyUpdaterSQL updater) {
        this.updater = updater;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyUnit, transactionAmount, addition);
    }

}
