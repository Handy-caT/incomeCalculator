package wallet.card;

import wallet.money.CurrencyConverter;
import wallet.money.Money;


public class AddTransaction extends Transaction {

    public AddTransaction(Card card, Money moneyAmount) {
        this.card = card;
        this.moneyAmount = moneyAmount;
    }

    @Override
    public void Execute() {
        if(card.currencyUnit.equals(moneyAmount.getCurrency())) {
            card.addMoneyToBalance(moneyAmount);
        } else {
            try {
                CurrencyConverter currencyConverter = CurrencyConverter.getInstance();
                Money convertedMoney = currencyConverter.convert(moneyAmount,card.currencyUnit);
                card.addMoneyToBalance(convertedMoney);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
