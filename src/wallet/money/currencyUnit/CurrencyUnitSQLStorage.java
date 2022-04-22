package wallet.money.currencyUnit;

import db.ConnectionFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class CurrencyUnitSQLStorage implements CurrencyUnitStorage {

    private static CurrencyUnitSQLStorage instance;
    private Connection dbConnection;

    public static String propertiesString = "properties/config.properties";
    public static final String defaultTableName = "currencyUnits";

    private static String tableName;

    private CurrencyUnitSQLStorage() throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();
        addTableNameToProperties(tableName);
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
        addTableNameToProperties(tableName);
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
        FileInputStream fis = new FileInputStream(propertiesString);
        Properties properties = new Properties();
        properties.load(fis);

        String tableName = (String) properties.get("CurrencyUnitSQLTableName");
        if(tableName == null) {
            return new CurrencyUnitSQLStorage();
        } else {
            return new CurrencyUnitSQLStorage(tableName);
        }
    }
    private static void addTableNameToProperties(String tableName) throws IOException {
        FileInputStream fis = new FileInputStream(propertiesString);
        Properties properties = new Properties();
        properties.load(fis);

        properties.put("CurrencyUnitSQLTableName",tableName);

        properties.store(new FileOutputStream(propertiesString),null);
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
            dbConnection.close();
            result = new StrictCurrencyUnit(currencyString,resultSet.getLong(1),resultSet.getLong(3));
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
            dbConnection.close();
            result = new StrictCurrencyUnit(resultSet.getString(2),currencyId,resultSet.getLong(3));
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
