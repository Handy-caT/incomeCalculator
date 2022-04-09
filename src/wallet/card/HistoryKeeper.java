package wallet.card;

import java.io.IOException;

public interface HistoryKeeper {

    void saveTransaction(Card.Memento snapshot);
    void saveState();

}
