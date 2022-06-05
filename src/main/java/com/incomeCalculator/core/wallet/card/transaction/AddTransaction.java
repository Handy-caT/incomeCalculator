package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.card.CardProvider;
import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.Money;

public class AddTransaction implements Transaction{

    protected final Money transactionAmount;
    public AddTransaction(Money money) {
        transactionAmount = money;
    }

    @Override
    public void execute(CardProvider card) {
        if(transactionAmount.getCurrency().equals(card.getCurrencyUnit())) {
            card.addMoneyToBalance(transactionAmount);
        } else {
            CurrencyConverter converter = CurrencyConverter.getInstance();
            Money convertedMoney = converter.convert(transactionAmount,card.getCurrencyUnit());
            card.addMoneyToBalance(convertedMoney);
        }
    }

    @Override
    public Money getTransactionAmount() {
        return transactionAmount;
    }

    @Override
    public Transaction revert() {
        return new ReduceTransaction(transactionAmount);
    }

    @Override
    public String toString() {
        return transactionAmount.toString();
    }
}
