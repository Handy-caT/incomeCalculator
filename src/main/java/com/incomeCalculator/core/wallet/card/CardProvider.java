package com.incomeCalculator.core.wallet.card;

import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.money.currencyUnit.CurrencyUnit;

public interface CardProvider {

    void receiveTransaction(Transaction transaction);
    void addMoneyToBalance(Money money);
    void subtractMoneyFromBalance(Money money);
    CurrencyUnit getCurrencyUnit();
    Money getBalance();

}
