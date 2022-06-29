package com.incomeCalculator.core.wallet.card.historyKeeper;

import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;

public abstract class HistoryKeeper {


    public abstract void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount);
    public abstract void restoreTransaction(Card card, Transaction transaction);

}
