package wallet.card.transaction;

import org.json.simple.parser.ParseException;
import wallet.card.Card;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

import java.io.IOException;

public class ReduceTransaction extends Transaction{

    ReduceTransaction(Money money) {
        super(money);
    }

    @Override
    public void execute(Card card) {
        if(transactionAmount.getCurrency().equals(card.getCurrencyUnit())) {
            card.subtractMoneyFromBalance(transactionAmount);
        } else {
            try {
                CurrencyConverter converter = CurrencyConverter.getInstance();
                Money convertedMoney = converter.convert(transactionAmount,card.getCurrencyUnit());
                card.subtractMoneyFromBalance(convertedMoney);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
