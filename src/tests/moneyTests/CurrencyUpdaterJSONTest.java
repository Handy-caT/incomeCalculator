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
        Assert.assertEquals(c.getCurScale("AMD"), BigDecimal.valueOf(1000));
        Assert.assertEquals(c.getCurScale("UAH"), BigDecimal.valueOf(100));
        Assert.assertEquals(c.getCurScale("USD"), BigDecimal.valueOf(1));
        Assert.assertEquals(c.getCurScale("EUR"), BigDecimal.valueOf(1));
        Assert.assertEquals(c.getCurScale("PLN"), BigDecimal.valueOf(10));
        Assert.assertEquals(c.getCurScale("JPY"), BigDecimal.valueOf(100));
        Assert.assertEquals(c.getCurScale("CAD"), BigDecimal.valueOf(1));
        Assert.assertEquals(c.getCurScale("CNY"), BigDecimal.valueOf(10));
    }

    @Test
    public void getRatio() throws IOException, ParseException {
        CurrencyUpdaterJSON c = CurrencyUpdaterJSON.getInstance();
        Assert.assertEquals(c.getRatio("BYN","CAD"), BigDecimal.valueOf(2.3577));
        Assert.assertEquals(c.getRatio("BYN","EUR"), BigDecimal.valueOf(3.2631));
        Assert.assertEquals(c.getRatio("BYN","USD"), BigDecimal.valueOf(2.9436));
        Assert.assertEquals(c.getRatio("BYN","PLN"), BigDecimal.valueOf(7.0029));
    }

    @Test
    public void getCurID() {
    }

    @Test
    public void getCurrencyRatiosMap() {
    }
}