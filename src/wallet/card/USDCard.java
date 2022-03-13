package wallet.card;

import wallet.Currency;
import wallet.balance.Balance;

public class USDCard implements Card {

    Balance balance;
    Currency currency = Currency.USD;

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public Balance getBalance() {
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
