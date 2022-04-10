package wallet.card;


import wallet.money.Money;
import wallet.money.StrictCurrencyUnit;

import java.math.BigDecimal;

public class Card {

    private Money balance;
    protected StrictCurrencyUnit currencyUnit;

    protected HistoryKeeper historyKeeper;

    public Card(HistoryKeeper historyKeeper,StrictCurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
        this.historyKeeper = historyKeeper;
        this.balance = Money.of(currencyUnit, BigDecimal.ZERO);
    }
    public Card(HistoryKeeper historyKeeper,StrictCurrencyUnit currencyUnit,Money balance) {
        this.currencyUnit = currencyUnit;
        this.historyKeeper = historyKeeper;
        this.balance = balance;
    }

    public void receiveTransaction(Transaction transaction) {
        Money beforeBalance = balance;
        transaction.Execute();
        Memento snapshot = new Memento(beforeBalance,balance,transaction.moneyAmount);
        historyKeeper.saveTransaction(snapshot);
    }
    protected void addMoneyToBalance(Money money) {
        balance = balance.plus(money);
    }
    protected void subtractMoneyFromBalance(Money money) {balance = balance.minus(money);}

    public Money getBalance() {
        return balance;
    }

    protected class Memento {
        Money beforeBalance;
        Money afterBalance;
        Money transactionAmount;

        Memento(Money beforeBalance,Money afterBalance, Money transactionAmount) {
            this.afterBalance = afterBalance;
            this.beforeBalance = beforeBalance;
            this.transactionAmount = transactionAmount;
        }

        public void restore() {

        }
    }

}
