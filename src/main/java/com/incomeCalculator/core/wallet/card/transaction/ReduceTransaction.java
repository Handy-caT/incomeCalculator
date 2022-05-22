package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.Money;

public class ReduceTransaction extends Transaction{

    public ReduceTransaction(Money money) {
        super(money);
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

}
