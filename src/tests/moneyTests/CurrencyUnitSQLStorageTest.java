package tests.moneyTests;

import db.ConnectionFactory;
import org.junit.*;
import wallet.money.currencyUnit.CurrencyUnitSQLStorage;
import wallet.money.currencyUpdater.CurrencyUpdaterJSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.Objects;

import static org.junit.Assert.*;

public class CurrencyUnitSQLStorageTest {

    @Before
    public void before() {
        ConnectionFactory.propertiesString = "testFiles/properties/config.properties";
    }

    public boolean containsUnit(String currencyString, Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT currencyId, currencyName, "
                + "currencyScale FROM " + tableName + " WHERE currencyName = ?");
        preparedStatement.setString(1, currencyString);
        ResultSet resultSet = preparedStatement.executeQuery();


        return Objects.equals(resultSet.getString(2), currencyString);
    }

    @Test
    public void ConstructorTest() throws SQLException, IOException {
        Files.copy(Paths.get("testFiles/properties/configConstructor.properties"),
                Paths.get("testFiles/properties/configConstructorTest.properties"), StandardCopyOption.REPLACE_EXISTING);
        CurrencyUnitSQLStorage.propertiesString = "testFiles/properties/configConstructorTest.properties";
        CurrencyUnitSQLStorage storage = CurrencyUnitSQLStorage.getInstance();
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        Assert.assertTrue(containsUnit("USD", connection, CurrencyUnitSQLStorage.defaultTableName));
        Assert.assertTrue(containsUnit("EUR", connection, CurrencyUnitSQLStorage.defaultTableName));
        Assert.assertTrue(containsUnit("RUB", connection, CurrencyUnitSQLStorage.defaultTableName));
        Assert.assertTrue(containsUnit("AMD", connection, CurrencyUnitSQLStorage.defaultTableName));
        Assert.assertTrue(containsUnit("UAH", connection, CurrencyUnitSQLStorage.defaultTableName));
        Assert.assertTrue(containsUnit("PLN", connection, CurrencyUnitSQLStorage.defaultTableName));
        connection.close();

        File file = new File("testFiles/properties/configConstructorTest.properties");
        //file.delete();
    }

    @Test
    public void ExistingTest() throws SQLException, IOException {
        CurrencyUnitSQLStorage.propertiesString = "testFiles/properties/config.properties";
        CurrencyUnitSQLStorage storage = CurrencyUnitSQLStorage.getInstance();
        String tableName = storage.getTableName();

        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        Assert.assertTrue(containsUnit("USD", connection, tableName));
        Assert.assertTrue(containsUnit("EUR", connection, tableName));
        Assert.assertTrue(containsUnit("RUB", connection, tableName));
        Assert.assertTrue(containsUnit("AMD", connection, tableName));
        Assert.assertTrue(containsUnit("UAH", connection, tableName));
        Assert.assertTrue(containsUnit("PLN", connection, tableName));
        connection.close();

    }

    @AfterClass
    public static void after() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE " + CurrencyUnitSQLStorage.defaultTableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}