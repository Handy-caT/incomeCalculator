package tests.moneyTests;

import db.ConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.money.currencyUnit.CurrencyUnitSQLStorage;
import wallet.money.currencyUpdater.CurrencyUpdaterJSON;

import java.sql.*;
import java.util.Objects;

import static org.junit.Assert.*;

public class CurrencyUnitSQLStorageTest {

    @Before
    public void before() {
        ConnectionFactory.propertiesString = "testFiles/properties/config.properties";
        CurrencyUnitSQLStorage.propertiesString = "testFiles/properties/config.properties";
    }

    public boolean containsUnit(String currencyString, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT currencyId, currencyName, "
                                        + "currencyScale FROM currencyUnits WHERE currencyName = ?");
        preparedStatement.setString(1,currencyString);
        ResultSet resultSet = preparedStatement.executeQuery();


        return Objects.equals(resultSet.getString(2), currencyString);
    }

    @Test
    public void ConstructorTest() throws SQLException {

        CurrencyUnitSQLStorage storage = CurrencyUnitSQLStorage.getInstance();
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        Assert.assertTrue(containsUnit("USD",connection));
        Assert.assertTrue(containsUnit("EUR",connection));
        Assert.assertTrue(containsUnit("ALL",connection));
        Assert.assertTrue(containsUnit("RUB",connection));
        Assert.assertTrue(containsUnit("AMD",connection));
        Assert.assertTrue(containsUnit("UAH",connection));
        Assert.assertTrue(containsUnit("VES",connection));
        connection.close();
    }

    @After
    public void after() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE " + "CurrencyUnits");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}