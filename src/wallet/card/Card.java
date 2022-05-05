package wallet.card;

import org.json.simple.parser.ParseException;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;
import wallet.money.currencyUnit.StrictCurrencyUnit;

import java.io.IOException;
import java.math.BigDecimal;

public class Card {

    private Money balance;
    protected StrictCurrencyUnit currencyUnit;

    public Card(StrictCurrencyUnit currencyUnit) {
        balance = Money.of(currencyUnit, BigDecimal.ZERO);
        this.currencyUnit = currencyUnit;
    }
    public Card(StrictCurrencyUnit currencyUnit, Money balance) throws IOException, ParseException {
        this.currencyUnit = currencyUnit;
        if(balance.getCurrency().equals(currencyUnit)) {
            this.balance = balance;
        } else {
            CurrencyConverter converter = CurrencyConverter.getInstance();
            this.balance = converter.convert(balance,currencyUnit);
        }
    }

    public void receiveTransaction(Transaction transaction) {
        transaction.execute(this);
    }

}
