package wallet.money.currencyUpdater;

import db.ConnectionFactory;
import wallet.PropertiesStorage;
import wallet.money.currencyUpdater.builders.CurrencyUpdaterSQLBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CurrencyUpdaterSQL implements CurrencyUpdaterProvider {

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private static String dateString;

    public static final String defaultTableName = "currencyRatios";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private CurrencyUpdaterSQL() throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();
        propertiesStorage.addProperty("CurrencyUpdater",tableName+dateString);

        CurrencyUpdaterSQLBuilder builder = CurrencyUpdaterSQLBuilder.getInstance(tableName,dbConnection);
        List<String> buildingPlan = builder.getBuildPlan();

        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        dbConnection.close();
    }
    private CurrencyUpdaterSQL(List<String> buildingPlan) throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();
        propertiesStorage.addProperty("CurrencyUpdater",tableName+dateString);

        CurrencyUpdaterSQLBuilder builder = CurrencyUpdaterSQLBuilder.getInstance(tableName,dbConnection);

        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        dbConnection.close();
    }
    private CurrencyUpdaterSQL(String tableName) {
        CurrencyUpdaterSQL.tableName = tableName;
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUpdaterSQL.tableName = defaultTableName;

        Date date = new Date();
        dateString = formatter.format(date);
        String sqlStatement = "CREATE TABLE " + tableName + dateString +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyFrom VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1," +
                "ratio DECIMAL(2,4) NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public long getCurScale(String currencyName) {
        ResultSet resultSet;
        long result = 0;
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        try {
            dbConnection = connectionFactory.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyId, currencyName, "
                    + "currencyScale FROM " + tableName + dateString + " WHERE currencyName = ?");
            preparedStatement.setString(1,currencyName);
            resultSet = preparedStatement.executeQuery();
            dbConnection.close();
            result = resultSet.getLong(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        return null;
    }

}
