package wallet.card.historyKeeper;

import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

public abstract class HistoryKeeper {


    public abstract void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount);
    public abstract void restoreTransaction(Card card, Transaction transaction);

}
