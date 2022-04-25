package wallet.money.currencyUpdater;

import db.ConnectionFactory;
import wallet.PropertiesStorage;
import wallet.money.currencyUpdater.builders.CurrencyUpdaterSQLBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CurrencyUpdaterSQL implements CurrencyUpdaterProvider {

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private static CurrencyUpdaterDateStorageSQL dateStorageSQL;

    private static String dateString;

    public static final String defaultTableName = "currencyRatios";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private CurrencyUpdaterSQL() throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
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
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
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
    private CurrencyUpdaterSQL(String tableName) throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
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

    private ResultSet getCurrencyResultSet(String currencyName) throws SQLException {
        ResultSet resultSet;
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyId, currencyName, "
                + "currencyScale FROM " + tableName + dateString + " WHERE currencyName = ?");
        preparedStatement.setString(1, currencyName);
        resultSet = preparedStatement.executeQuery();
        dbConnection.close();
        return resultSet;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio = null;

        if(Objects.equals(currencyFrom,currencyTo)) {
            return BigDecimal.ONE;
        } else {
            try {
                if (!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
                    ResultSet fromSet = getCurrencyResultSet(currencyFrom);
                    long scaleFrom = fromSet.getLong(2);
                    ratio = fromSet.getBigDecimal(3);

                    ResultSet toSet = getCurrencyResultSet(currencyTo);
                    long scaleTo = toSet.getLong(2);
                    BigDecimal secondRatio = toSet.getBigDecimal(3);

                    ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
                    ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
                    ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

                } else if(Objects.equals(currencyFrom, "BYN")) {
                    ResultSet toSet = getCurrencyResultSet(currencyTo);
                    ratio = toSet.getBigDecimal(3);
                    long scaleTo = toSet.getLong(2);
                    ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
                    ratio.multiply(BigDecimal.valueOf(scaleTo));
                } else {
                    ResultSet fromSet = getCurrencyResultSet(currencyFrom);
                    long scaleFrom = fromSet.getLong(2);
                    ratio = fromSet.getBigDecimal(3);
                    ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return ratio;
    }

    @Override
    public long getCurScale(String currencyName) {
        ResultSet resultSet;
        long result = 0;
        try {
            resultSet = getCurrencyResultSet(currencyName);
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
