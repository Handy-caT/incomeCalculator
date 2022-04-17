package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    String dbUrl;
    public String propertiesString = "properties/config.properties";

    private ConnectionFactory() {
        Properties properties = new Properties();
        try(FileInputStream fis = new FileInputStream(propertiesString)) {
            properties.load(fis);
            dbUrl = (String) properties.get("DatabaseUrl");
            String dbDriver = (String) properties.get("DatabaseDriver");
            Class.forName(dbDriver);
        } catch (IOException e) {
            e.printStackTrace();
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
