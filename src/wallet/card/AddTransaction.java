package wallet.card;

import wallet.money.Money;

public class AddTransaction extends Transaction {

    AddTransaction(Card card, Money moneyAmount) {
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
}
