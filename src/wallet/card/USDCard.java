package wallet.card;

import wallet.money.CurrencyUnit;
import wallet.money.Money;

public class USDCard implements Card {

    Money balance;
    CurrencyUnit currency = CurrencyUnit.of("USD");

    @Override
    public CurrencyUnit getCurrency() {
        return currency;
    }

    @Override
    public Money getBalance() {
        return balance;
    }

    @Override
    public float getMonthConsumptiion() {
        return 0;
    }

    @Override
    public float getMonthIncome() {
        return 0;
    }
}
