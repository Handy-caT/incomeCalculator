package wallet.money.currencyUnit.currencyUnitSQL;

import db.ConnectionFactory;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import wallet.money.currencyUnit.StrictCurrencyUnit;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitSQLStorage implements CurrencyUnitStorage {

    private static CurrencyUnitSQLStorage instance;
    private Connection dbConnection;

    public static final String defaultTableName = "currencyUnits";
    public static final String propertyName = "CurrencyUnitSQLTableName";

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static String tableName;

    private CurrencyUnitSQLStorage() throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();
        propertiesStorage.addProperty(propertyName,tableName);

        CurrencyUnitSQLStorageBuilder builder = CurrencyUnitSQLStorageBuilder.getInstance(tableName,dbConnection);
        List<String> buildingPlan = builder.getBuildPlan();

        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        dbConnection.close();
    }
    private CurrencyUnitSQLStorage(List<String> buildingPlan) throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();
        propertiesStorage.addProperty(propertyName,tableName);
        CurrencyUnitSQLStorageBuilder builder = CurrencyUnitSQLStorageBuilder.getInstance(tableName,dbConnection);

        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        dbConnection.close();
    }
    private CurrencyUnitSQLStorage(String tableName) {
        CurrencyUnitSQLStorage.tableName = tableName;
    }

    private static CurrencyUnitSQLStorage createInstance() throws IOException, SQLException {
        String tableName = (String) propertiesStorage.getProperty(propertyName);
        if(tableName == null) {
            return new CurrencyUnitSQLStorage();
        } else {
            return new CurrencyUnitSQLStorage(tableName);
        }
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUnitSQLStorage.tableName = defaultTableName;
        String sqlStatement = "CREATE TABLE " + tableName + " (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyId BIGINT," +
                "currencyName VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1)";
         statement.executeUpdate(sqlStatement);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        ResultSet resultSet;
        StrictCurrencyUnit result = null;
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        try {
            dbConnection = connectionFactory.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyId, currencyName, "
                    + "currencyScale FROM " + tableName + " WHERE currencyName = ?");
            preparedStatement.setString(1,currencyString);
            resultSet = preparedStatement.executeQuery();
            result = new StrictCurrencyUnit(currencyString,resultSet.getLong(1),resultSet.getLong(3));
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyId, currencyName, "
                    + "currencyScale FROM " + tableName + " WHERE currencyName = ?");
            preparedStatement.setString(1, currencyString);
            ResultSet resultSet = preparedStatement.executeQuery();

            return Objects.equals(resultSet.getString(2), currencyString);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId) {
        ResultSet resultSet;
        StrictCurrencyUnit result = null;
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        try {
            dbConnection = connectionFactory.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyId, currencyName, "
                    + "currencyScale FROM " + tableName + " WHERE currencyId = ?");
            preparedStatement.setLong(1,currencyId);
            resultSet = preparedStatement.executeQuery();
            result = new StrictCurrencyUnit(resultSet.getString(2),currencyId,resultSet.getLong(3));
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static CurrencyUnitSQLStorage getInstance() throws SQLException, IOException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public String getTableName() {
        return tableName;
    }

}
