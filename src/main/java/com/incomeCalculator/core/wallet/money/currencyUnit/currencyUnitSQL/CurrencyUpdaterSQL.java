package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import com.incomeCalculator.core.db.ConnectionFactory;
import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class CurrencyUpdaterSQL implements CurrencyUpdater {

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat sqlFormatter = new SimpleDateFormat("dd_MM_yyyy");
    private static CurrencyUpdaterDateStorageSQL dateStorageSQL;

    private static String dateString;

    public static final String defaultTableName = "currencyRatios";
    public static final String propertyName = "CurrencyUpdaterSQLTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    protected CurrencyUpdaterSQL() throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        Date date = new Date();
        dateString = sqlFormatter.format(date);

        createTable(dateString);
        propertiesStorage.addProperty(propertyName,tableName+dateString);
        if(!dateStorageSQL.isUpdaterExist(tableName+dateString)) {
            CurrencyUpdaterSQLBuilder builder = new CurrencyUpdaterSQLBuilder(tableName, dbConnection,dateString);
            List<String> buildingPlan = builder.getBuildPlan();

            for (String currencyString : buildingPlan) {
                builder.buildCurrency(currencyString);
            }
            dateStorageSQL.addUpdater(tableName + dateString);
        }
        dbConnection.close();
    }
    protected CurrencyUpdaterSQL(List<String> buildingPlan) throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        Date date = new Date();
        dateString = sqlFormatter.format(date);

        createTable(dateString);
        propertiesStorage.addProperty(propertyName,tableName+dateString);

        CurrencyUpdaterSQLBuilder builder = new CurrencyUpdaterSQLBuilder(tableName, dbConnection,dateString);

        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        dbConnection.close();
    }
    protected CurrencyUpdaterSQL(String tableName) throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
        CurrencyUpdaterSQL.tableName = tableName.substring(0,tableName.length()-10);
        CurrencyUpdaterSQL.dateString = tableName.substring(tableName.length()-10);

        Date date = new Date();
        String dateString = sqlFormatter.format(date);
        if(!dateString.equals(CurrencyUpdaterSQL.dateString)) {
            if (!dateStorageSQL.isUpdaterExist(CurrencyUpdaterSQL.tableName + dateString)) {
                ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
                dbConnection = connectionFactory.getConnection();

                createTable(dateString);

                CurrencyUpdaterSQLBuilder builder = new CurrencyUpdaterSQLBuilder(CurrencyUpdaterSQL.tableName, dbConnection,dateString);
                List<String> buildingPlan = builder.getBuildPlan();

                for (String currencyString : buildingPlan) {
                    builder.buildCurrency(currencyString);
                }
                dbConnection.close();
                dateStorageSQL.addUpdater(CurrencyUpdaterSQL.tableName + dateString);
            }

            CurrencyUpdaterSQL.dateString = dateString;
        }
    }

    private void createTable(String dateString) throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUpdaterSQL.tableName = defaultTableName;

        String sqlStatement = "CREATE TABLE " + tableName + dateString +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyFrom VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1," +
                "ratio DECIMAL(2,4) NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

    public void createUpdaterOnDate(Date date) throws SQLException {
        String dateString = sqlFormatter.format(date);
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        createTable(dateString);

        String webDateString = webFormatter.format(date);
        CurrencyUpdaterSQLBuilder builder = new CurrencyUpdaterSQLBuilder(tableName, dbConnection, webDateString);
        List<String> buildingPlan = builder.getBuildPlan();

        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        dbConnection.close();

        dateStorageSQL.addUpdater(tableName+dateString);
    }
    public boolean isUpdaterOnDateExist(Date date) {
        return dateStorageSQL.isUpdaterExist(tableName + sqlFormatter.format(date));
    }

    private List<Object> getCurrencyResultSet(String currencyName, String dateString) throws SQLException {
        ResultSet resultSet;
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();

        PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT currencyFrom , currencyScale, "
                + " ratio FROM " + tableName + dateString + " WHERE currencyFrom = ?");
        preparedStatement.setString(1, currencyName);
        resultSet = preparedStatement.executeQuery();

        List<Object> list = new LinkedList<>();
        list.add("0");
        list.add(resultSet.getString(1));
        list.add(resultSet.getLong(2));
        list.add(resultSet.getDouble(3));

        dbConnection.close();
        return list;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio = null;

        if(Objects.equals(currencyFrom,currencyTo)) {
            return BigDecimal.ONE;
        } else {
            try {
                if (!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
                    List<Object> fromList = getCurrencyResultSet(currencyFrom,dateString);
                    long scaleFrom = (long) fromList.get(2);
                    ratio = BigDecimal.valueOf((double)fromList.get(3));

                    List<Object> toList = getCurrencyResultSet(currencyTo,dateString);
                    long scaleTo = (long) toList.get(2);
                    BigDecimal secondRatio = BigDecimal.valueOf((double)toList.get(3));

                    ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
                    ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
                    ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

                } else if(Objects.equals(currencyFrom, "BYN")) {
                    List<Object> toList = getCurrencyResultSet(currencyTo,dateString);
                    long scaleTo = (long) toList.get(2);
                    ratio = BigDecimal.valueOf((double)toList.get(3));

                    ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
                    ratio.multiply(BigDecimal.valueOf(scaleTo));
                } else {
                    List<Object> fromList = getCurrencyResultSet(currencyFrom,dateString);
                    long scaleFrom = (long) fromList.get(2);
                    ratio = BigDecimal.valueOf((double)fromList.get(3));
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
        long result = 0;
        try {
            List<Object> resultList = getCurrencyResultSet(currencyName,dateString);
            result = (long) resultList.get(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        String dateString = sqlFormatter.format(date);

        if(!dateStorageSQL.isUpdaterExist(tableName+dateString)) {
            try {
                createUpdaterOnDate(date);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String temp = CurrencyUpdaterSQL.dateString;
        CurrencyUpdaterSQL.dateString = dateString;
        BigDecimal result = getRatio(currencyFrom,currencyTo);
        CurrencyUpdaterSQL.dateString = temp;
        return result;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyToList) {
        Map<String,BigDecimal> currenciesHash = new HashMap<>();
        for(String currencyTo : currencyToList) {
            currenciesHash.put(currencyTo,getRatio(currencyFrom,currencyTo));
        }
        return currenciesHash;
    }

    public String getTableName() {
        return tableName + dateString;
    }


}
