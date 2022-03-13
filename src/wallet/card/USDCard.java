package wallet.card;

import wallet.Currency;
import wallet.money.Money;

public class USDCard implements Card {

    Money balance;
    Currency currency = Currency.USD;

    @Override
    public Currency getCurrency() {
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
