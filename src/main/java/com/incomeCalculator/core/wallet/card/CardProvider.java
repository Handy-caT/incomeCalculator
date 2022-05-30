package com.incomeCalculator.core.wallet.card;

import com.incomeCalculator.core.wallet.card.transaction.Transaction;
import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;

public interface CardProvider {

    void receiveTransaction(Transaction transaction);
    void addMoneyToBalance(Money money);
    void subtractMoneyFromBalance(Money money);
    StrictCurrencyUnit getCurrencyUnit();
    Money getBalance();

}
