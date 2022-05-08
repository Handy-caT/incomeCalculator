package wallet.card.historyKeeper;

import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.Money;

public interface HistoryKeeper {

    void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount);
    void restoreTransaction(Card card, Transaction transaction);

}
