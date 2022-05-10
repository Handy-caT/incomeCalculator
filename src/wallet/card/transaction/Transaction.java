package wallet.card.transaction;

import wallet.card.Card;
import wallet.money.Money;

public abstract class Transaction {

    protected final Money transactionAmount;
    Transaction(Money money) {
        this.transactionAmount = money;
    }
    public abstract void execute(Card card);
    public Money getTransactionAmount() {
        return transactionAmount;
    }

}
