package wallet.card;


import wallet.money.CurrencyUnit;
import wallet.money.Money;

public interface Card {

    CurrencyUnit getCurrency();
    Money getBalance();
    float getMonthConsumption();
    float getMonthIncome();

}
