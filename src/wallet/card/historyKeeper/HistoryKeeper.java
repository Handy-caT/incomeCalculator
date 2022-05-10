package wallet.card.historyKeeper;

import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

public abstract class HistoryKeeper {

    protected void restoreTransaction(Card card, Transaction transaction, Money beforeBalance, Money afterBalance) {
        if(afterBalance.compareTo(beforeBalance) > 0) {
            if(transaction.getTransactionAmount().isSameCurrency(afterBalance)) {
                card.subtractMoneyFromBalance(transaction.getTransactionAmount());
            } else {
                CurrencyConverter converter = CurrencyConverter.getInstance();
                Money convertedMoney = converter.convert(transaction.getTransactionAmount(),
                        card.getCurrencyUnit());
                card.subtractMoneyFromBalance(convertedMoney);
            }
        } else {
            if (transaction.getTransactionAmount().isSameCurrency(afterBalance)) {
                card.addMoneyToBalance(transaction.getTransactionAmount());
            } else {
                CurrencyConverter converter = CurrencyConverter.getInstance();
                Money convertedMoney = converter.convert(transaction.getTransactionAmount(),
                        card.getCurrencyUnit());
                card.addMoneyToBalance(convertedMoney);
            }
        }
    }

    public abstract void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount);
    public abstract void restoreTransaction(Card card, Transaction transaction);

}
