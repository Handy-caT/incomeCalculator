package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import com.incomeCalculator.core.db.ConnectionFactory;
import com.incomeCalculator.core.wallet.PropertiesStorage;

import java.io.IOException;
import java.sql.*;

public class CurrencyUpdaterDateStorageSQL {

    private Connection dbConnection;
    private static String tableName;
    private static CurrencyUpdaterDateStorageSQL instance;
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    public static final String propertyName = "CurrencyUpdatersStorageTableName";
    public static final String defaultTableName = "currencyUpdatersStorage";

    private CurrencyUpdaterDateStorageSQL() throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();

        propertiesStorage.addProperty(propertyName, tableName);

        dbConnection.close();
    }
    private  CurrencyUpdaterDateStorageSQL(String tableName) {
        CurrencyUpdaterDateStorageSQL.tableName = tableName;
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUpdaterDateStorageSQL.tableName = defaultTableName;
        String sqlStatement = "CREATE TABLE " + tableName +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "updaterTableName VARCHAR(24) NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

    public void addUpdater(String tableName) throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        PreparedStatement preparedStatement = dbConnection.prepareStatement
                ("INSERT INTO " + CurrencyUpdaterDateStorageSQL.tableName +
                " (updaterTableName) VALUES (?)");
        preparedStatement.setString(1,tableName);
        preparedStatement.executeUpdate();

        dbConnection.close();
    }

    public boolean isUpdaterExist(String tableName) {
        try {
            ResultSet resultSet;
            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            dbConnection = connectionFactory.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT updaterTableName "
                    + "FROM " + tableName + " WHERE updaterTableName = ?");
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();
            resultSet.getString(1);
            dbConnection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static CurrencyUpdaterDateStorageSQL createInstance() throws SQLException, IOException {
        String tableName = (String) propertiesStorage.getProperty(propertyName);
        if(tableName == null) {
            return new CurrencyUpdaterDateStorageSQL();
        } else {
            return new CurrencyUpdaterDateStorageSQL(tableName);
        }
    }

    public static CurrencyUpdaterDateStorageSQL getInstance() throws SQLException, IOException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }

}
