package wallet.card.transaction;

import wallet.card.Card;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

public class ReduceTransaction extends Transaction {

    public ReduceTransaction(Money moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public void Execute(Card card) {
        if(card.currencyUnit.equals(moneyAmount.getCurrency())) {
            card.subtractMoneyFromBalance(moneyAmount);
        } else {
            try {
                CurrencyConverter currencyConverter = CurrencyConverter.getInstance();
                Money convertedMoney = currencyConverter.convert(moneyAmount,card.currencyUnit);
                card.subtractMoneyFromBalance(convertedMoney);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}