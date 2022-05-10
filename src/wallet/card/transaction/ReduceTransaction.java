package wallet.card.transaction;

import wallet.card.Card;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

public class ReduceTransaction extends Transaction{

    ReduceTransaction(Money money) {
        super(money);
    }

    @Override
    public void execute(Card card) {
        if(transactionAmount.getCurrency().equals(card.getCurrencyUnit())) {
            card.subtractMoneyFromBalance(transactionAmount);
        } else {
            CurrencyConverter converter = CurrencyConverter.getInstance();
            Money convertedMoney = converter.convert(transactionAmount,card.getCurrencyUnit());
            card.subtractMoneyFromBalance(convertedMoney);
        }
    }

}
