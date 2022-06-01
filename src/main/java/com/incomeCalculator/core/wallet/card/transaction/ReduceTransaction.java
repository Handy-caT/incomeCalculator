package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.Money;

public class ReduceTransaction implements Transaction{

    protected final Money transactionAmount;
    public ReduceTransaction(Money money) {
        transactionAmount = money;
    }

    @Override
    public void execute(Card card) {
        if(transactionAmount.getCurrency().equals(card.getCurrencyUnit())) {
            card.subtractMoneyFromBalance(transactionAmount);
        } else {
            CurrencyConverter converter = CurrencyConverter.getInstance();
            Money convertedMoney = converter.convert(transactionAmount,card.getCurrencyUnit());
            card.subtractMoneyFromBalance(convertedMoney);
        }
    }

    @Override
    public Money getTransactionAmount() {
        return transactionAmount;
    }

    @Override
    public Transaction revert() {
        return new AddTransaction(transactionAmount);
    }

    @Override
    public String toString() {
        return transactionAmount.toString();
    }

}
