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
        Memento snapshot = new Memento(beforeBalance,balance);
    }
    protected void addMoneyToBalance(Money money) {
        balance = balance.plus(money);
    }
    protected

    class Memento {
        Money beforeBalance;
        Money afterBalance;
        Money transactionAmount;

        Memento(Money beforeBalance,Money afterBalance) {
            this.afterBalance = afterBalance;
            this.beforeBalance = beforeBalance;
            this.transactionAmount = afterBalance.minus(beforeBalance);
        }

        public void restore() {

        }
    }

}
