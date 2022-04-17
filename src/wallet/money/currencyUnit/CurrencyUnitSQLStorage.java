package wallet.money.currencyUnit;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CurrencyUnitSQLStorage implements CurrencyUnitStorage {

    private CurrencyUnitSQLStorage instance;
    private Connection dbConnection;

    public static String propertiesString = "properties/config.properties";

    private CurrencyUnitSQLStorage() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();

    }
    private CurrencyUnitSQLStorage(List<String> buildingPlan) {


    }
    private CurrencyUnitSQLStorage(String tableName) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.execute("USE " + tableName);
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        String sqlStatement = "CREATE TABLE currencyUnits(" +
                "currencyId BIGINT PRIMARY KEY," +
                "currencyName VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1)";
        statement.execute(sqlStatement);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        return null;
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        return false;
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId) {
        return null;
    }

}
