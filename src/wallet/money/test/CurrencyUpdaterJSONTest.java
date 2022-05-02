package wallet.money.test;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;

import java.io.IOException;

public class CurrencyUpdaterJSONTest {

    static PropertiesStorage propertiesStorage;

    @BeforeClass
    public static void before() throws IOException {
        propertiesStorage = PropertiesStorage.getInstance();
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");
    }
    @Test
    public void getCurScale() throws IOException, ParseException {
        CurrencyUpdaterJSON c = CurrencyUpdaterJSON.getInstance();
        Assert.assertEquals(c.getCurScale("AMD"), 1000);
        Assert.assertEquals(c.getCurScale("UAH"), 100);
        Assert.assertEquals(c.getCurScale("USD"), 1);
        Assert.assertEquals(c.getCurScale("EUR"), 1);
        Assert.assertEquals(c.getCurScale("PLN"), 10);
        Assert.assertEquals(c.getCurScale("JPY"), 100);
        Assert.assertEquals(c.getCurScale("CAD"), 1);
        Assert.assertEquals(c.getCurScale("CNY"), 10);
    }

    @Test
    public void getCurrencyRatiosMap() {
    }
}