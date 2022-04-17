package tests.moneyTests;

import db.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import wallet.money.currencyUnit.CurrencyUnitSQLStorage;
import wallet.money.currencyUpdater.CurrencyUpdaterJSON;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CurrencyUnitSQLStorageTest {

    @Before
    public void before() {
        CurrencyUpdaterJSON.propertiesString = "testFiles/properties/config.properties";
    }

    @Test
    public void ConstructorTest() throws SQLException {

        CurrencyUnitSQLStorage storage = CurrencyUnitSQLStorage.getInstance();
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();
        connection.close();
    }
}