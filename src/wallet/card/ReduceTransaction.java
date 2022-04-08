package wallet.card;

import wallet.money.CurrencyConverter;
import wallet.money.Money;

public class ReduceTransaction extends Transaction {

    ReduceTransaction(Card card, Money moneyAmount) {
        this.card = card;
        this.moneyAmount = moneyAmount;
    }

    @Override
    public void Execute() {
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
