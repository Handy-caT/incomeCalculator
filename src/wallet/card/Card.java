package wallet.card;


import wallet.Currency;

public interface Card {

    void setCurrency(Currency currency);
    Currency getCurrency();

}
