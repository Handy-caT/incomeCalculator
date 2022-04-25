package tests.moneyTests;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.money.currencyUpdater.CurrencyUpdaterJSON;

import java.io.IOException;
import java.math.BigDecimal;

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
    public void getRatio() throws IOException, ParseException {
        CurrencyUpdaterJSON c = CurrencyUpdaterJSON.getInstance();
        Assert.assertEquals(c.getRatio("CAD","BYN"), BigDecimal.valueOf(2.3577));
        Assert.assertEquals(c.getRatio("EUR","BYN"), BigDecimal.valueOf(3.2631));
        Assert.assertEquals(c.getRatio("USD","BYN"), BigDecimal.valueOf(2.9436));
        Assert.assertEquals(c.getRatio("PLN","BYN"), BigDecimal.valueOf(7.0029));
    }

    @Test
    public void getCurID() {
    }

    @Test
    public void getCurrencyRatiosMap() {
    }
}