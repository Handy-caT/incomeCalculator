package wallet.money.currencyUnit.currencyUnitSQL;

import db.ConnectionFactory;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class CurrencyUpdaterSQL implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterSQL instance;

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat sqlFormatter = new SimpleDateFormat("dd_MM_yyyy");
    private static CurrencyUpdaterDateStorageSQL dateStorageSQL;

    private static String dateString;

    public static final String defaultTableName = "currencyRatios";
    public static final String propertyName = "CurrencyUpdaterSQLTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private CurrencyUpdaterSQL() throws SQLException, IOException {
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
        }
        dbConnection.close();
    }
    private CurrencyUpdaterSQL(List<String> buildingPlan) throws SQLException, IOException {
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
    private CurrencyUpdaterSQL(String tableName) throws SQLException, IOException {
        dateStorageSQL = CurrencyUpdaterDateStorageSQL.getInstance();
        CurrencyUpdaterSQL.tableName = tableName;
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
    private static CurrencyUpdaterSQL createInstance() throws SQLException, IOException {
        String tableName = (String) propertiesStorage.getProperty(propertyName);
        if(tableName == null) {
            return new CurrencyUpdaterSQL();
        } else {
            return new CurrencyUpdaterSQL(tableName);
        }
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

    private ResultSet getCurrencyResultSet(String currencyName, String dateString) throws SQLException {
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
                    ResultSet fromSet = getCurrencyResultSet(currencyFrom,dateString);
                    long scaleFrom = fromSet.getLong(2);
                    ratio = fromSet.getBigDecimal(3);

                    ResultSet toSet = getCurrencyResultSet(currencyTo,dateString);
                    long scaleTo = toSet.getLong(2);
                    BigDecimal secondRatio = toSet.getBigDecimal(3);

                    ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
                    ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
                    ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

                } else if(Objects.equals(currencyFrom, "BYN")) {
                    ResultSet toSet = getCurrencyResultSet(currencyTo,dateString);
                    ratio = toSet.getBigDecimal(3);
                    long scaleTo = toSet.getLong(2);
                    ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
                    ratio.multiply(BigDecimal.valueOf(scaleTo));
                } else {
                    ResultSet fromSet = getCurrencyResultSet(currencyFrom,dateString);
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
            resultSet = getCurrencyResultSet(currencyName,dateString);
            result = resultSet.getLong(2);
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

    public static CurrencyUpdaterSQL getInstance() throws SQLException, IOException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public static String getTableName() {
        return tableName;
    }


}
