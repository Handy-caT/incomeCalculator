package wallet.card.historyKeeper;

import wallet.card.Card;
import wallet.card.transaction.Transaction;

public interface HistoryKeeper {

    void saveTransaction(Card.Memento snapshot);
    void saveState();
    Transaction getTransaction(String id);
    void deleteTransaction(String id);

}
