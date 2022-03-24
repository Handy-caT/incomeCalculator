package tests.moneyTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.money.CurrencyUpdaterJSON;
import wallet.money.CurrencyUpdaterProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import static org.junit.Assert.*;


public class CurrencyUpdaterJSONTest {

    CurrencyUpdaterProvider currencyUpdater;

    void addAddFile() throws IOException {
        Path copied = Paths.get("testFiles/currenciesToAddTest.json");
        Path origin = Paths.get("testFiles/currenciesToAdd.json");
        Files.copy(origin,copied,StandardCopyOption.REPLACE_EXISTING);
    }

    void addDeleteFile() throws IOException {
        Path copied = Paths.get("testFiles/currenciesToDeleteTest.json");
        Path origin = Paths.get("testFiles/currenciesToDelete.json");
        Files.copy(origin,copied,StandardCopyOption.REPLACE_EXISTING);
    }

    @Before
    public void setUp() throws Exception {
        // /Users/maksim/IdeaProjects/incomeCalculator/testFiles/currenciesToAdd.json
        currencyUpdater = new CurrencyUpdaterJSON();
        addAddFile();
        addDeleteFile();
    }

    @Test
    public void getDecimalPlaces() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToAdd.json");
        BigDecimal decimalPlaces = currencyUpdater.getDecimalPlaces("USD");
        Assert.assertEquals(BigDecimal.valueOf(2),decimalPlaces);
    }

    @Test
    public void getRatio() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToAdd.json");
        BigDecimal ratio = currencyUpdater.getRatio("USD","EUR");
        Assert.assertEquals(BigDecimal.valueOf(0.96),ratio);
    }

    @Test
    public void getCurrencyHash() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToAdd.json");
        HashMap<String,BigDecimal> hash = currencyUpdater.getCurrencyHash("EUR");
        Assert.assertTrue(hash.containsKey("EUR"));
        Assert.assertTrue(hash.containsKey("BYN"));
        Assert.assertTrue(hash.containsKey("USD"));
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

        //currencyUpdater.addCurrency("GBP",BigDecimal.valueOf(2),currencyHash);

        BigDecimal ratio = currencyUpdater.getRatio("GBP","USD");
        Assert.assertEquals(USDRatio,ratio);
        ratio = currencyUpdater.getRatio("GBP","EUR");
        Assert.assertEquals(EURRatio,ratio);
        ratio = currencyUpdater.getRatio("GBP","BYN");
        Assert.assertEquals(BYNRatio,ratio);

    }

    @Test
    public void addRatio() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToAddTest.json");
        BigDecimal EURToGBPRatio = BigDecimal.valueOf(0.8395);
        //currencyUpdater.addRatio("EUR","GBP",EURToGBPRatio);
        BigDecimal ratio = currencyUpdater.getRatio("EUR","GBP");
        Assert.assertEquals(EURToGBPRatio,ratio);
    }

    @Test
    public void deleteRatio() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToDeleteTest.json");
        //currencyUpdater.deleteRatio("EUR","GBP");
        BigDecimal ratio = currencyUpdater.getRatio("EUR","GBP");
        Assert.assertNull(ratio);
    }

    @Test
    public void deleteCurrency() {
        CurrencyUpdaterJSON.setJsonFilePath("testFiles/currenciesToDeleteTest.json");
        //currencyUpdater.deleteCurrency("USD");
        BigDecimal ratio = currencyUpdater.getRatio("USD","EUR");
        //Assert.assertNull(ratio);
        ratio = currencyUpdater.getRatio("USD","BYN");
        //Assert.assertNull(ratio);
    }
}