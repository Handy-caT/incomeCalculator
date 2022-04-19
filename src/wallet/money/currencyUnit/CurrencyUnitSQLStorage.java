package wallet.money.currencyUnit;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CurrencyUnitSQLStorage implements CurrencyUnitStorage {

    private static CurrencyUnitSQLStorage instance;
    private Connection dbConnection;

    public static String propertiesString = "properties/config.properties";

    private CurrencyUnitSQLStorage() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();

        CurrencyUnitSQLStorageBuilder builder = CurrencyUnitSQLStorageBuilder.getInstance("currencyUnits",dbConnection);
        List<String> buildingPlan = builder.getBuildPlan();

        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
    }
    private CurrencyUnitSQLStorage(List<String> buildingPlan) {


    }
    private CurrencyUnitSQLStorage(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        String sqlStatement = "CREATE TABLE currencyUnits (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyId BIGINT," +
                "currencyName VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1)";
        statement.executeUpdate(sqlStatement);
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

    public static CurrencyUnitSQLStorage getInstance() throws SQLException {
        if(instance == null) {
            instance = new CurrencyUnitSQLStorage();
        }
        return instance;
    }

}
