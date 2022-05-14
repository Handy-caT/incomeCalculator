package test;

import db.ConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import wallet.money.util.WebApiJSON;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrencyUpdaterSQLTest {

    static PropertiesStorage propertiesStorage;
    private static final String jsonPath = "testFiles/json/testapi.json";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static final String onDateString = "testFiles/json/OnDate_testapi.json";
    private static TestAPI testAPI;

    private void copyTable(String tableName) throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE " + tableName +
                    " AS SELECT * " +
                    "FROM currencyRatios26_04_2022");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }
    private void copyUpdatersStorage() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE currencyUpdatersStorageTest"+
                    " AS SELECT * " +
                    "FROM currencyUpdatersStorage");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    private void checkScales(CurrencyUpdaterSQL updater) {
        Assert.assertEquals(updater.getCurScale("EUR"),1);
        Assert.assertEquals(updater.getCurScale("USD"),1);
        Assert.assertEquals(updater.getCurScale("CAD"),1);
        Assert.assertEquals(updater.getCurScale("PLN"),10);
        Assert.assertEquals(updater.getCurScale("UAH"),100);
        Assert.assertEquals(updater.getCurScale("RUB"),100);
    }
    private void checkNewRatios(CurrencyUpdaterSQL updater) {
        MathContext m = new MathContext(5);

        Assert.assertEquals(updater.getRatio("EUR","BYN"), BigDecimal.valueOf(2.7896));
        Assert.assertEquals(updater.getRatio("USD","BYN"), BigDecimal.valueOf(2.6534));
        Assert.assertEquals(updater.getRatio("CAD","BYN"), BigDecimal.valueOf(2.0662));
        Assert.assertEquals(updater.getRatio("PLN","BYN"), BigDecimal.valueOf(5.9465/10));
        Assert.assertEquals(updater.getRatio("UAH","BYN"),
                BigDecimal.valueOf(8.7716/100).round(m));
        Assert.assertEquals(updater.getRatio("RUB","BYN"),
                BigDecimal.valueOf(3.6694/100).round(m));
        Assert.assertEquals(updater.getRatio("BYN","EUR"), BigDecimal.valueOf(0.3584));
        Assert.assertEquals(updater.getRatio("EUR","USD"), BigDecimal.valueOf(1.0513));
    }
    private void checkOldRatios(CurrencyUpdaterSQL updater) {
        MathContext m = new MathContext(5);

        Assert.assertEquals(updater.getRatio("EUR","BYN"), BigDecimal.valueOf(2.8568));
        Assert.assertEquals(updater.getRatio("USD","BYN"), BigDecimal.valueOf(2.6662));
        Assert.assertEquals(updater.getRatio("CAD","BYN"), BigDecimal.valueOf(2.0908));
        Assert.assertEquals(updater.getRatio("PLN","BYN"), BigDecimal.valueOf(6.1547/10));
        Assert.assertEquals(updater.getRatio("UAH","BYN"),
                BigDecimal.valueOf(8.8139/100).round(m));
        Assert.assertEquals(updater.getRatio("RUB","BYN"),
                BigDecimal.valueOf(3.6334/100).round(m));
        Assert.assertEquals(updater.getRatio("BYN","EUR"), BigDecimal.valueOf(0.3500).setScale(4));
        Assert.assertEquals(updater.getRatio("EUR","USD"), BigDecimal.valueOf(1.0714));
    }

    @BeforeClass
    public static void before() {
        propertiesStorage = PropertiesStorage.getInstance();
    }

    @Test
    public void constructorTest() throws IOException, SQLException {
        testAPI = new TestAPI(jsonPath);
        WebApiJSON.setApi(testAPI);

        Files.copy(Paths.get("testFiles/properties/configConstructor.properties"),
                Paths.get("testFiles/properties/configConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);
        propertiesStorage.setPropertiesPath("testFiles/properties/configConstructorTest.properties");

        copyUpdatersStorage();

        CurrencyUpdaterSQLFactory factory = new CurrencyUpdaterSQLFactory();
        CurrencyUpdaterSQL updater = (CurrencyUpdaterSQL) factory.createUpdater();

        checkNewRatios(updater);
        checkScales(updater);

        File file = new File("testFiles/properties/configConstructorTest.properties");
        file.delete();
    }

    @Test
    public void existingWithoutUpdateConstructorTest() throws IOException, SQLException {
        Files.copy(Paths.get("testFiles/properties/configNewConstructor.properties"),
                Paths.get("testFiles/properties/configNewConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);
        propertiesStorage.setPropertiesPath("testFiles/properties/configNewConstructorTest.properties");

        Date date = new Date();
        copyTable(CurrencyUpdaterSQL.defaultTableName + formatter.format(date));
        copyUpdatersStorage();
        propertiesStorage.addProperty(CurrencyUpdaterSQL.propertyName,
                CurrencyUpdaterSQL.defaultTableName + formatter.format(date));

        CurrencyUpdaterSQLFactory factory = new CurrencyUpdaterSQLFactory();
        CurrencyUpdaterSQL updater = (CurrencyUpdaterSQL) factory.createUpdater();

        checkOldRatios(updater);
        checkScales(updater);

        File file = new File("testFiles/properties/configNewConstructorTest.properties");
        file.delete();
    }

    @Test
    public void existingWithUpdateConstructorTest() throws IOException, SQLException {
        testAPI = new TestAPI(jsonPath);
        WebApiJSON.setApi(testAPI);

        Files.copy(Paths.get("testFiles/properties/configOldConstructor.properties"),
                Paths.get("testFiles/properties/configOldConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);
        propertiesStorage.setPropertiesPath("testFiles/properties/configOldConstructorTest.properties");

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        Date dateOld = calendar.getTime();

        copyTable(CurrencyUpdaterSQL.defaultTableName + formatter.format(dateOld));
        copyUpdatersStorage();

        propertiesStorage.addProperty(CurrencyUpdaterSQL.propertyName,
                CurrencyUpdaterSQL.defaultTableName + formatter.format(dateOld));

        CurrencyUpdaterSQLFactory factory = new CurrencyUpdaterSQLFactory();
        CurrencyUpdaterSQL updater = (CurrencyUpdaterSQL) factory.createUpdater();

        checkNewRatios(updater);
        checkScales(updater);

        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE " +  CurrencyUpdaterSQL.defaultTableName + formatter.format(dateOld));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();

        File file = new File("testFiles/properties/configOldConstructorTest.properties");
        file.delete();
    }

    @After
    public void after() throws SQLException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE currencyRatios" + formatter.format(new Date()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();

        connection = connectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE currencyUpdatersStorageTest");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }


}