package wallet.money.test;

import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;

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
        CurrencyUpdaterSQL currencyUpdater = CurrencyUpdaterSQL.getInstance();

    }

    @Test
    public void getRatioOnDate() {
    }



}