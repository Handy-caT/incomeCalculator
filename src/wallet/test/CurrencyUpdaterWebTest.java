package wallet.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;

import java.math.BigDecimal;

public class CurrencyUpdaterWebTest {

    private TestAPI testAPI;
    private String jsonPath = "testFiles/json/testapi.json";

    @Before
    public void setUp() throws Exception {
        testAPI = new TestAPI(jsonPath);
        CurrencyUpdaterWeb.setApi(testAPI);
    }

    @Test
    public void getRatio() {
        CurrencyUpdaterWebFactory factory = new CurrencyUpdaterWebFactory();
        CurrencyUpdaterWeb updater = (CurrencyUpdaterWeb) factory.createUpdater();
        BigDecimal ratio = updater.getRatio("EUR","BYN");
        Assert.assertEquals(BigDecimal.valueOf(2.7896),ratio);
        ratio = updater.getRatio("USD","BYN");
        Assert.assertEquals(BigDecimal.valueOf(2.6534),ratio);
        ratio = updater.getRatio("RUB","BYN");
        Assert.assertEquals(BigDecimal.valueOf(3.6694/100),ratio);
    }
}