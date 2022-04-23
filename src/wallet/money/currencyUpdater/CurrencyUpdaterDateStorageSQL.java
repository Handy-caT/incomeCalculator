package wallet.money.currencyUpdater;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class CurrencyUpdaterDateStorageSQL {

    private Connection dbConnection;
    private static String tableName;

    public static final String defaultTableName = "currencyUpdatersStorage";

    private CurrencyUpdaterDateStorageSQL() {

    }
    private  CurrencyUpdaterDateStorageSQL(String tableName) {

    }

    private void createTable() throws SQLException {
        Statement statement = dbConnection.createStatement();
        CurrencyUpdaterDateStorageSQL.tableName = defaultTableName;
        String sqlStatement = "CREATE TABLE " + tableName +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "updaterTableName VARCHAR(24) NOT NULL)";
        statement.executeUpdate(sqlStatement);
    }

}
