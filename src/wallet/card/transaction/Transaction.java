package wallet.card.transaction;

import wallet.card.Card;
import wallet.money.Money;

public abstract class Transaction {
    public Money moneyAmount;

    public abstract void Execute(Card card);
}
