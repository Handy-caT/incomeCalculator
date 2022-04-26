package tests.moneyTests;

import db.ConnectionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.CurrencyUnitSQLStorage;
import wallet.money.currencyUpdater.CurrencyUpdaterSQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class CurrencyUpdaterSQLTest {

    static PropertiesStorage propertiesStorage;

    @BeforeClass
    public static void before() {
        propertiesStorage = PropertiesStorage.getInstance();
    }

    @Test
    public void getRatio() throws IOException, SQLException {
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");
        CurrencyUpdaterSQL currencyUpdater = CurrencyUpdaterSQL.getInstance();

    }

    @Test
    public void getRatioOnDate() {
    }



}