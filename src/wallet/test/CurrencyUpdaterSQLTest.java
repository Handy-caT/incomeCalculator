package wallet.test;

import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;

import java.io.IOException;
import java.sql.SQLException;

public class CurrencyUpdaterSQLTest {

    static PropertiesStorage propertiesStorage;

    @BeforeClass
    public static void before() {
        propertiesStorage = PropertiesStorage.getInstance();
    }

    @Test
    public void getRatio() throws IOException, SQLException {
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");
        CurrencyUpdaterSQLFactory factory = new CurrencyUpdaterSQLFactory();
        CurrencyUpdaterSQL currencyUpdater = (CurrencyUpdaterSQL) factory.createUpdater();

    }

    @Test
    public void getRatioOnDate() {
    }



}