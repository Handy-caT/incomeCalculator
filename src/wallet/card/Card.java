package wallet.card;

import wallet.card.historyKeeper.HistoryKeeper;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;
import wallet.money.currencyUnit.StrictCurrencyUnit;

import java.math.BigDecimal;

public class Card {

    private Money balance;
    protected final StrictCurrencyUnit currencyUnit;
    private final HistoryKeeper historyKeeper;

    public Card(StrictCurrencyUnit currencyUnit, HistoryKeeper historyKeeper) {
        this.historyKeeper = historyKeeper;

        balance = Money.of(currencyUnit, BigDecimal.ZERO);
        this.currencyUnit = currencyUnit;
    }
    public Card(StrictCurrencyUnit currencyUnit, Money balance, HistoryKeeper historyKeeper) {
        this.historyKeeper = historyKeeper;

        this.currencyUnit = currencyUnit;
        if(balance.getCurrency().equals(currencyUnit)) {
            this.balance = balance;
        } else {
            CurrencyConverter converter = CurrencyConverter.getInstance();
            this.balance = converter.convert(balance,currencyUnit);
        }
    }

    public void receiveTransaction(Transaction transaction) {
        Money beforeBalance = balance;
        transaction.execute(this);
        historyKeeper.saveState(beforeBalance,balance,transaction.getTransactionAmount());
    }

    public void addMoneyToBalance(Money money) {
        if(!money.isSameCurrency(balance)) {
            throw new IllegalArgumentException("Can't add not same currency");
        } else {
            balance = balance.plus(money);
        }
    }
    public void subtractMoneyFromBalance(Money money) {
        if(!money.isSameCurrency(balance)) {
            throw new IllegalArgumentException("Can't add not same currency");
        } else {
            balance = balance.plus(money);
        }
    }

    public StrictCurrencyUnit getCurrencyUnit() {
        return currencyUnit;
    }

    public Money getBalance() {
        return balance;
    }
}
