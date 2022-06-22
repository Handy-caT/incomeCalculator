package com.incomeCalculator.core.wallet.card.historyKeeper;

import com.incomeCalculator.core.wallet.money.Money;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.core.db.ConnectionFactory;
import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.card.Card;
import com.incomeCalculator.core.wallet.card.transaction.Transaction;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class SQLHistoryKeeper extends HistoryKeeper {

    private Connection dbConnection;
    private String tableName;
    private static String dateString;

    public static final String defaultTableName = "HistoryKeeper";
    public static final String propertyName = "HistoryKeeperTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    public SQLHistoryKeeper() throws SQLException, IOException {
        ConnectionFactory factory = ConnectionFactory.getInstance();
        dbConnection = factory.getConnection();

        createTable();
        propertiesStorage.addProperty(propertyName,tableName);
        dbConnection.close();
    }
    public SQLHistoryKeeper(String tableName) throws SQLException {
        this.tableName = tableName;

        ConnectionFactory factory = ConnectionFactory.getInstance();
        dbConnection = factory.getConnection();

        Date date = new Date();
        dateString = DateFormatter.sqlFormat(date);
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        this.tableName = defaultTableName;

        String sqlStatement = "CREATE TABLE " + tableName +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "beforeBalance VARCHAR(255) NOT NULL," +
                "afterBalance VARCHAR(255) NOT NULL," +
                "transactionAmount VARCHAR(255) NOT NULL," +
                "transactionDate DATETIME NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

    @Override
    public void restoreTransaction(Card card, Transaction transaction) {
        try {
            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            dbConnection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT beforeBalance, afterBalance," +
                    " transactionAmount,transactionDate FROM " + tableName + " WHERE transactionAmount = ?");
            preparedStatement.setString(1, transaction.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            card.receiveTransaction(transaction.revert());
        } catch (SQLException e) {
            throw new IllegalArgumentException("No transaction found");
        }
    }

    @Override
    public void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount) {
        ConnectionFactory factory = ConnectionFactory.getInstance();
        try {
            dbConnection = factory.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO " + tableName +
                    " (beforeBalance, afterBalance, transactionAmount,transactionDate) VALUES (?, ?, ?, ?)");

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            preparedStatement.setString(1,beforeBalance.toString());
            preparedStatement.setString(2,afterBalance.toString());
            preparedStatement.setString(3,transactionAmount.toString());
            preparedStatement.setObject(4,timestamp);

            preparedStatement.executeUpdate();

            dbConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
