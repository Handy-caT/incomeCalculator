package wallet.card;

import wallet.money.Money;

public abstract class Transaction {
    protected Money moneyAmount;

    abstract void Execute(Card card);
}
