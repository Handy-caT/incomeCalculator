package com.incomeCalculator.Core.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrencyUpdaterJSONTest {

    static PropertiesStorage propertiesStorage;
    private static final String testPath = "testFiles/json/";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static final String jsonPath = "testFiles/json/testapi.json";
    private static TestAPI testAPI;


    private void checkScales(CurrencyUpdaterJSON updater) {
        Assert.assertEquals(updater.getCurScale("EUR"),1);
        Assert.assertEquals(updater.getCurScale("USD"),1);
        Assert.assertEquals(updater.getCurScale("CAD"),1);
        Assert.assertEquals(updater.getCurScale("PLN"),10);
        Assert.assertEquals(updater.getCurScale("UAH"),100);
        Assert.assertEquals(updater.getCurScale("RUB"),100);
    }
    private void checkNewRatios(CurrencyUpdaterJSON updater) {
        MathContext m5 = new MathContext(5);

        Assert.assertEquals(updater.getRatio("EUR","BYN"), BigDecimal.valueOf(2.7896));
        Assert.assertEquals(updater.getRatio("USD","BYN"), BigDecimal.valueOf(2.6534));
        Assert.assertEquals(updater.getRatio("CAD","BYN"), BigDecimal.valueOf(2.0662));
        Assert.assertEquals(updater.getRatio("PLN","BYN"),
                BigDecimal.valueOf(5.9465/10));
        Assert.assertEquals(updater.getRatio("UAH","BYN"),
                BigDecimal.valueOf(8.7716/100).round(m5));
        Assert.assertEquals(updater.getRatio("RUB","BYN"),
                BigDecimal.valueOf(3.6694/100).round(m5));
        Assert.assertEquals(updater.getRatio("BYN","EUR"), BigDecimal.valueOf(0.3585));
        Assert.assertEquals(updater.getRatio("EUR","USD"), BigDecimal.valueOf(1.0513));
    }
    private void checkOldRatios(CurrencyUpdaterJSON updater) {
        MathContext m = new MathContext(5);

        Assert.assertEquals(updater.getRatio("EUR","BYN"), BigDecimal.valueOf(2.8785));
        Assert.assertEquals(updater.getRatio("USD","BYN"), BigDecimal.valueOf(2.6685));
        Assert.assertEquals(updater.getRatio("CAD","BYN"), BigDecimal.valueOf(2.105));
        Assert.assertEquals(updater.getRatio("PLN","BYN"), BigDecimal.valueOf(6.2162/10));
        Assert.assertEquals(updater.getRatio("UAH","BYN"),
                BigDecimal.valueOf(9.0765/100).round(m));
        Assert.assertEquals(updater.getRatio("RUB","BYN"),
                BigDecimal.valueOf(3.6227/100).round(m));
        Assert.assertEquals(updater.getRatio("BYN","EUR"), BigDecimal.valueOf(0.3474));
        Assert.assertEquals(updater.getRatio("EUR","USD"), BigDecimal.valueOf(1.0787));
    }

    @BeforeClass
    public static void before() throws IOException {
        propertiesStorage = PropertiesStorage.getInstance();
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");

        CurrencyUpdaterJSON.setDir("testFiles/json/");
    }

    @Test
    public void existingWithoutUpdateConstructorTest() throws IOException {
        Files.copy(Paths.get("testFiles/properties/configConstructor.properties"),
                Paths.get("testFiles/properties/configConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);

        Date date = new Date();
        File jsonFile = new File(testPath + CurrencyUpdaterJSON.defaultFileName
                + formatter.format(date) + ".json");
        Files.copy(Paths.get("testFiles/json/currencyUpdater02_05_2022Test.json"),
                jsonFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        propertiesStorage.setPropertiesPath("testFiles/properties/configConstructorTest.properties");
        propertiesStorage.addProperty(CurrencyUpdaterJSON.propertyName,jsonFile.toString());

        CurrencyUpdaterJSONFactory factory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterJSON updater = (CurrencyUpdaterJSON) factory.createUpdater();

        checkOldRatios(updater);
        checkScales(updater);

        Assert.assertTrue(jsonFile.exists());
        jsonFile.delete();

        File file = new File("testFiles/properties/configConstructorTest.properties");
        file.delete();
    }

    @Test
    public void existingWithUpdateConstructorTest() throws IOException {
        testAPI = new TestAPI(jsonPath);
        WebApiJSON.setApi(testAPI);

        Files.copy(Paths.get("testFiles/properties/configOldConstructor.properties"),
                Paths.get("testFiles/properties/configOldConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        Date dateOld = calendar.getTime();

        File jsonFileOld = new File(testPath + CurrencyUpdaterJSON.defaultFileName
                + formatter.format(dateOld) + ".json");
        Files.copy(Paths.get("testFiles/json/currencyUpdater02_05_2022Test.json"),
                jsonFileOld.toPath(), StandardCopyOption.REPLACE_EXISTING);

        propertiesStorage.setPropertiesPath("testFiles/properties/configOldConstructorTest.properties");
        propertiesStorage.addProperty(CurrencyUpdaterJSON.propertyName,jsonFileOld.toString());

        CurrencyUpdaterJSONFactory factory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterJSON updater = (CurrencyUpdaterJSON) factory.createUpdater();

        checkNewRatios(updater);
        checkScales(updater);

        File jsonFile = new File(testPath + CurrencyUpdaterJSON.defaultFileName
                + formatter.format(date) + ".json");
        Assert.assertTrue(jsonFile.exists());
        jsonFile.delete();

        File file = new File("testFiles/properties/configOldConstructorTest.properties");
        file.delete();
    }

    @Test
    public void constructorTest() throws IOException {
        testAPI = new TestAPI(jsonPath);
        WebApiJSON.setApi(testAPI);

        Files.copy(Paths.get("testFiles/properties/configNewConstructor.properties"),
                Paths.get("testFiles/properties/configNewConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);
        propertiesStorage.setPropertiesPath("testFiles/properties/configNewConstructorTest.properties");

        CurrencyUpdaterJSONFactory factory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterJSON updater = (CurrencyUpdaterJSON) factory.createUpdater();

        checkNewRatios(updater);
        checkScales(updater);

        Date date = new Date();
        File jsonFile = new File(testPath + CurrencyUpdaterJSON.defaultFileName
                + formatter.format(date) + ".json");
        Assert.assertTrue(jsonFile.exists());
        jsonFile.delete();

        File file = new File("testFiles/properties/configNewConstructorTest.properties");
        file.delete();

    }

    @Test
    public void onDateTest() throws IOException {
        testAPI = new TestAPI(jsonPath);
        String onDateString = "testFiles/json/OnDate_testapi.json";
        testAPI.setonDateString(onDateString);
        WebApiJSON.setApi(testAPI);
        CurrencyUpdaterWeb.setApi(testAPI);


        Files.copy(Paths.get("testFiles/properties/configConstructor.properties"),
                Paths.get("testFiles/properties/configConstructorTest.properties"),
                StandardCopyOption.REPLACE_EXISTING);

        Date date = new Date();
        File jsonFile = new File(testPath + CurrencyUpdaterJSON.defaultFileName
                + formatter.format(date) + ".json");
        Files.copy(Paths.get("testFiles/json/currencyUpdater02_05_2022Test.json"),
                jsonFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        propertiesStorage.setPropertiesPath("testFiles/properties/configConstructorTest.properties");
        propertiesStorage.addProperty(CurrencyUpdaterJSON.propertyName,jsonFile.toString());

        CurrencyUpdaterJSONFactory factory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterJSON updater = (CurrencyUpdaterJSON) factory.createUpdater();

        MathContext m = new MathContext(5);

        Assert.assertEquals(updater.getRatioOnDate("EUR","BYN",new Date()), BigDecimal.valueOf(2.8785));
        Assert.assertEquals(updater.getRatioOnDate("USD","BYN",new Date()), BigDecimal.valueOf(2.6685));
        Assert.assertEquals(updater.getRatioOnDate("CAD","BYN",new Date()), BigDecimal.valueOf(2.105));
        Assert.assertEquals(updater.getRatioOnDate("PLN","BYN",new Date()), BigDecimal.valueOf(6.2162/10));
        Assert.assertEquals(updater.getRatioOnDate("UAH","BYN",new Date()),
                BigDecimal.valueOf(9.0765/100).round(m));
        Assert.assertEquals(updater.getRatioOnDate("RUB","BYN",new Date()),
                BigDecimal.valueOf(3.6227/100).round(m));

        jsonFile.delete();

        File file = new File("testFiles/properties/configConstructorTest.properties");
        file.delete();
    }


}