package wallet.money;

import wallet.Currency;
import wallet.CurrencyUnit;

import java.math.BigDecimal;

public class Money {

    private CurrencyUnit currency;
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public Money(CurrencyUnit currency,BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public static Money of(CurrencyUnit currency,BigDecimal amount) {
        return new Money(currency,amount);
    }

    public static Money parse(String moneyString) {
        String formattedMoneyString = moneyString.replaceAll(" ","");
        if(formattedMoneyString.length() < 4){
            throw new IllegalArgumentException(moneyString.trim() + " can't be parsed");
        }
        String currencyString = formattedMoneyString.substring(0,3);
        String amountString = formattedMoneyString.substring(4);
        return of(CurrencyUnit.of(currencyString),new BigDecimal(amountString));
    }
}
