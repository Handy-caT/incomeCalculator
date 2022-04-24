package wallet.money.currencyUpdater;

import db.ConnectionFactory;
import wallet.PropertiesStorage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrencyUpdaterDateStorageSQL {

    private Connection dbConnection;
    private static String tableName;
    private static CurrencyUpdaterDateStorageSQL instance;
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    public static final String defaultTableName = "currencyUpdatersStorage";

    private CurrencyUpdaterDateStorageSQL() throws SQLException, IOException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        dbConnection = connectionFactory.getConnection();
        createTable();

        propertiesStorage.addProperty("CurrencyUpdatersStorageTableName", tableName);

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

    private static CurrencyUpdaterDateStorageSQL createInstance() throws SQLException, IOException {
        String tableName = (String) propertiesStorage.getProperty("CurrencyUpdatersStorageTableName");
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
