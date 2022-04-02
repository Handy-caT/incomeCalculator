package wallet.card;

public interface HistoryKeeper {

    void addTransaction(Transaction transaction);
    void saveState(Transaction transaction);

}
