package wallet.card;


import wallet.money.Money;
import wallet.money.StrictCurrencyUnit;

public class Card {

    private Money balance;
    protected StrictCurrencyUnit currencyUnit;

    protected HistoryKeeper historyKeeper;

    public void receiveTransaction(Transaction transaction) {
        Money beforeBalance = balance;
        transaction.Execute();
        Memento snapshot = new Memento(beforeBalance,balance,transaction.moneyAmount);
    }
    protected void addMoneyToBalance(Money money) {
        balance = balance.plus(money);
    }
    protected void subtractMoneyFromBalance(Money money) {balance = balance.minus(money);}

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
