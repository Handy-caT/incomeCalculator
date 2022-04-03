package wallet.card;

import wallet.money.Money;

public abstract class Transaction {
    protected Card card;
    protected Money moneyAmount;

    abstract void Execute();
}
