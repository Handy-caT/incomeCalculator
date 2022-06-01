package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.card.CardProvider;
import com.incomeCalculator.core.wallet.money.Money;

public interface Transaction {


    void execute(CardProvider card);
    Money getTransactionAmount();

    Transaction revert();

}

