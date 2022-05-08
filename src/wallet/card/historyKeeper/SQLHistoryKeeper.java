package wallet.card.historyKeeper;

import wallet.PropertiesStorage;
import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.Money;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterDateStorageSQL;

import java.sql.Connection;
import java.text.SimpleDateFormat;

public class SQLHistoryKeeper implements HistoryKeeper {

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat sqlFormatter = new SimpleDateFormat("dd_MM_yyyy");

    private static String dateString;

    public static final String defaultTableName = "currencyRatios";
    public static final String propertyName = "CurrencyUpdaterSQLTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    @Override
    public void restoreTransaction(Card card, Transaction transaction) {

    }

    @Override
    public void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount) {

    }
}
