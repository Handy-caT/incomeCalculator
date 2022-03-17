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
    }
}