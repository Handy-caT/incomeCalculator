package tests.moneyTests;

import org.junit.Before;
import org.junit.Test;
import wallet.money.CurrencyUpdaterJSON;
import wallet.money.CurrencyUpdaterProvider;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CurrencyUpdaterJSONTest {

    CurrencyUpdaterProvider currencyUpdater;

    @Before
    public void setUp() throws Exception {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currencies.json");
        // /Users/maksim/IdeaProjects/incomeCalculator/testFiles/currencies.json
        currencyUpdater = new CurrencyUpdaterJSON();
    }

    @Test
    public void getDecimalPlaces() {
        BigDecimal decimalPlaces = currencyUpdater.getDecimalPlaces("USD");
        assertEquals(BigDecimal.valueOf(2),decimalPlaces);
    }

    @Test
    public void getRatio() {
        BigDecimal ratio = currencyUpdater.getRatio("USD","EUR");
        assertEquals(BigDecimal.valueOf(0.96),ratio);
    }

    @Test
    public void getCurrencyHash() {
        HashMap<String,BigDecimal> hash = currencyUpdater.getCurrencyHash("EUR");
        assertTrue(hash.containsKey("EUR"));
        assertTrue(hash.containsKey("BYN"));
        assertTrue(hash.containsKey("USD"));
    }

    @Test
    public void addCurrency() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToAddTest.json");
        HashMap<String,BigDecimal> currencyHash = new HashMap<>();

        BigDecimal USDRatio = BigDecimal.valueOf(1.3171);
        BigDecimal EURRatio = BigDecimal.valueOf(1.1913);
        BigDecimal BYNRatio = BigDecimal.valueOf(4.3433);

        currencyHash.put("USD",USDRatio);
        currencyHash.put("EUR",EURRatio);
        currencyHash.put("BYN",BYNRatio);

        currencyUpdater.addCurrency("GBP",BigDecimal.valueOf(2),currencyHash);

        BigDecimal ratio = currencyUpdater.getRatio("GBP","USD");
        assertEquals(USDRatio,ratio);
        ratio = currencyUpdater.getRatio("GBP","EUR");
        assertEquals(EURRatio,ratio);
        ratio = currencyUpdater.getRatio("GBP","BYN");
        assertEquals(BYNRatio,ratio);

    }
}