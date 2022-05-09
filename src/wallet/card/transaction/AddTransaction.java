package wallet.card.transaction;

import org.json.simple.parser.ParseException;
import wallet.card.Card;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

import java.io.IOException;

public class AddTransaction extends Transaction{

    public AddTransaction(Money money) {
        super(money);
    }

    @Override
    public void execute(Card card) {
        if(transactionAmount.getCurrency().equals(card.getCurrencyUnit())) {
            card.addMoneyToBalance(transactionAmount);
        } else {
            try {
                CurrencyConverter converter = CurrencyConverter.getInstance();
                Money convertedMoney = converter.convert(transactionAmount,card.getCurrencyUnit());
                card.addMoneyToBalance(convertedMoney);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
