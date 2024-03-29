package com.incomeCalculator.Core.test;

import com.incomeCalculator.core.db.ConnectionFactory;
import org.junit.*;
import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL.CurrencyUnitSQLStorageFactory;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL.CurrencyUnitSQLStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.Objects;

public class CurrencyUnitSQLStorageConstructorTest {

    static PropertiesStorage propertiesStorage;
    static String dbPath = "testFiles/db/test.db";
    static String jdbc = "jdbc:sqlite:";

    @BeforeClass
    public static void before() {
        propertiesStorage = PropertiesStorage.getInstance();
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
        propertiesStorage.setPropertiesPath("testFiles/properties/configConstructorTest.properties");
        File dbfile = new File(dbPath);
        propertiesStorage.addProperty("DatabaseUrl",jdbc + dbfile.getAbsolutePath());

        String jsonPath = "testFiles/json/testapi.json";
        TestAPI testAPI = new TestAPI(jsonPath);
        WebApiJSON.setApi(testAPI);

        CurrencyUnitSQLStorageFactory factory = new CurrencyUnitSQLStorageFactory();
        CurrencyUnitSQLStorage storage = (CurrencyUnitSQLStorage) factory.createStorage();

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
        file.delete();
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