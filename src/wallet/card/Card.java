package wallet.card;


import wallet.money.Money;
import wallet.money.StrictCurrencyUnit;

public class Card {

    private Money balance;
    protected StrictCurrencyUnit currencyUnit;

    protected HistoryKeeper historyKeeper;

    protected void addMoneyToBalance(Money money) {
        balance = balance.plus(money);
    }

}
