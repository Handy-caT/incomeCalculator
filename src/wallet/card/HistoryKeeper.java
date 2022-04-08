package wallet.card;

public interface HistoryKeeper {

    void saveTransaction(Card.Memento snapshot);

}
