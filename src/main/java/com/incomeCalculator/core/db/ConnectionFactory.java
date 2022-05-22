package com.incomeCalculator.core.db;

import com.incomeCalculator.core.wallet.PropertiesStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    private String dbUrl;
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private ConnectionFactory() {
        try {
            dbUrl = (String) propertiesStorage.getProperty("DatabaseUrl");
            String dbDriver = (String) propertiesStorage.getProperty("DatabaseDriver");
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
}
