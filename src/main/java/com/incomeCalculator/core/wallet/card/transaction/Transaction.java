package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.money.Money;

public interface Transaction {


    void execute(Card card);
    Money getTransactionAmount();

    Transaction revert();

}

