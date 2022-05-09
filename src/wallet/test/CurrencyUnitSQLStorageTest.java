package wallet.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUnitSQLStorage;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUnitSQLStorageFactory;

import java.io.IOException;
import java.sql.SQLException;

public class CurrencyUnitSQLStorageTest {

    static PropertiesStorage propertiesStorage;
    static CurrencyUnitSQLStorageFactory factory;

    @BeforeClass
    public static void before() throws IOException {
        propertiesStorage = PropertiesStorage.getInstance();
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");
        factory = new CurrencyUnitSQLStorageFactory();
    }

    @Test
    public void getCurrencyUnitByCurrencyString() throws SQLException, IOException {
        CurrencyUnitSQLStorage storage = (CurrencyUnitSQLStorage) factory.createStorage();

        Assert.assertEquals("USD",
                storage.getCurrencyUnitByCurrencyString("USD").getCurrencyName());
        Assert.assertEquals("EUR",
                storage.getCurrencyUnitByCurrencyString("EUR").getCurrencyName());
        Assert.assertEquals("RUB",
                storage.getCurrencyUnitByCurrencyString("RUB").getCurrencyName());
        Assert.assertEquals("CAD",
                storage.getCurrencyUnitByCurrencyString("CAD").getCurrencyName());
        Assert.assertEquals("UAH",
                storage.getCurrencyUnitByCurrencyString("UAH").getCurrencyName());
        Assert.assertEquals("XDR",
                storage.getCurrencyUnitByCurrencyString("XDR").getCurrencyName());

    }

    @Test
    public void isCurrencyExists() throws SQLException, IOException {
        CurrencyUnitSQLStorage storage = (CurrencyUnitSQLStorage) factory.createStorage();

        Assert.assertFalse(storage.isCurrencyExists("ABC"));
        Assert.assertFalse(storage.isCurrencyExists("ABCD"));
        Assert.assertFalse(storage.isCurrencyExists("AB"));
        Assert.assertFalse(storage.isCurrencyExists("abc"));
        Assert.assertFalse(storage.isCurrencyExists("eur"));
        Assert.assertFalse(storage.isCurrencyExists(null));
    }

    @Test
    public void getCurrencyUnitByCurrencyID() throws SQLException, IOException {
        CurrencyUnitSQLStorage storage = (CurrencyUnitSQLStorage) factory.createStorage();

        Assert.assertEquals(456,storage.getCurrencyUnitByCurrencyID(456).getCurrencyId());
        Assert.assertEquals(457,storage.getCurrencyUnitByCurrencyID(457).getCurrencyId());
        Assert.assertEquals(449,storage.getCurrencyUnitByCurrencyID(449).getCurrencyId());
        Assert.assertEquals(431,storage.getCurrencyUnitByCurrencyID(431).getCurrencyId());
        Assert.assertEquals(451,storage.getCurrencyUnitByCurrencyID(451).getCurrencyId());
    }
}