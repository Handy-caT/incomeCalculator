package com.incomeCalculator.core.wallet.card.transaction;

import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.money.Money;

public abstract class Transaction {

    protected final Money transactionAmount;
    Transaction(Money money) {
        this.transactionAmount = money;
    }
    public abstract void execute(Card card);
    public Money getTransactionAmount() {
        return transactionAmount;
    }

    public Transaction revert() {
        if(this.getClass() == AddTransaction.class) {
            return new ReduceTransaction(transactionAmount);
        } else if(this.getClass() == ReduceTransaction.class) {
            return  new AddTransaction(transactionAmount);
        } else {
            throw new IllegalArgumentException("Unknown transaction type");
        }
    }

    @Override
    public String toString() {
        return transactionAmount.toString();
    }
}

