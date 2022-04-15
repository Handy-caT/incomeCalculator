package wallet.card;


import wallet.money.Money;
import wallet.money.StrictCurrencyUnit;

import java.math.BigDecimal;

public class Card {

    private Money balance;
    protected StrictCurrencyUnit currencyUnit;
    protected String id;

    protected HistoryKeeper historyKeeper;
    protected IdGenerator idGenerator;

    public Card(HistoryKeeper historyKeeper,StrictCurrencyUnit currencyUnit,String id) {
        this.currencyUnit = currencyUnit;
        this.historyKeeper = historyKeeper;
        this.balance = Money.of(currencyUnit, BigDecimal.ZERO);

        this.id = id;
        this.idGenerator = new IdGenerator(this);
    }
    public Card(HistoryKeeper historyKeeper,StrictCurrencyUnit currencyUnit,Money balance,String id) {
        this.currencyUnit = currencyUnit;
        this.historyKeeper = historyKeeper;
        this.balance = balance;

        this.id = id;
        this.idGenerator = new IdGenerator(this);
    }

    public void receiveTransaction(Transaction transaction) {
        Money beforeBalance = balance;
        transaction.Execute(this);
        String id = idGenerator.getId(transaction);
        Memento snapshot = new Memento(beforeBalance,balance,transaction.moneyAmount,id);
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
        String id;

        Money beforeBalance;
        Money afterBalance;
        Money transactionAmount;

        Memento(Money beforeBalance,Money afterBalance, Money transactionAmount, String id) {
            this.afterBalance = afterBalance;
            this.beforeBalance = beforeBalance;
            this.transactionAmount = transactionAmount;

            this.id = id;
        }

        public void restore(String id) {
            Transaction restoreTransaction = historyKeeper.getTransaction(id);
            receiveTransaction(restoreTransaction);
        }
    }

}
