package wallet.card;

import wallet.money.Money;

public class ReceiveTransaction implements Transaction {
    protected Card card;
    protected Money moneyAmount;

    ReceiveTransaction(Card card, Money moneyAmount) {
        this.card = card;
        this.moneyAmount = moneyAmount;
    }

    @Override
    public void Execute() {
        if(card.currencyUnit.equals(moneyAmount.getCurrency())) {
            card.addMoneyToBalance(moneyAmount);
        } else {

        }
    }

    class Memento {
        Money beforeBalance;
        Money afterBalance;

        Memento(Money beforeBalance, Money afterBalance, Money transactionAmount) {

        }
    }
}
