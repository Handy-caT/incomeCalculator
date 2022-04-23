package wallet.money.currencyUpdater;

import wallet.money.currencyUnit.CurrencyUnitSQLStorage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CurrencyUpdaterSQL implements CurrencyUpdaterProvider {

    private Connection dbConnection;
    private static String tableName;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static final String defaultTableName = "currencyRatios";


    private CurrencyUpdaterSQL() {

    }
    private CurrencyUpdaterSQL(List<String> buildingPlan) {

    }
    private CurrencyUpdaterSQL(String tableName) {
        CurrencyUpdaterSQL.tableName = tableName;
    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUpdaterSQL.tableName = defaultTableName;

        Date date = new Date();

        String sqlStatement = "CREATE TABLE " + tableName + formatter.format(date) +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyFrom VARCHAR(3) NOT NULL," +
                "currencyTo VARCHAR(3) NOT NULL," +
                "currencyScale BIGINT NOT NULL DEFAULT 1," +
                "ratio DECIMAL(2,4) NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public BigDecimal getCurScale(String currencyName) {
        return null;
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
