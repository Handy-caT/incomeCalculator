package wallet.card;


import wallet.Currency;
import wallet.balance.Balance;

public interface Card {

    Currency getCurrency();
    Balance getBalance();
    float getMonthConsumptiion();
    float getMonthIncome();

}
