package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.card.CardProvider;

public interface Transaction {


    void execute(CardProvider card);
    Money getTransactionAmount();

    Transaction revert();

}

