package wallet.card;


import wallet.Currency;
import wallet.money.Money;

public interface Card {

    Currency getCurrency();
    Money getBalance();
    float getMonthConsumptiion();
    float getMonthIncome();

}
