package wallet.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.card.Card;
import wallet.card.historyKeeper.JSONHistoryKeeper;
import wallet.card.transaction.AddTransaction;
import wallet.card.transaction.Transaction;
import wallet.money.Money;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUnitJSONStorage;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUnitJSONStorageFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class CardTest {

    static SecureRandom random;
    static String configString = "testFiles/properties/config.properties";
    static String testConfigString = "testFiles/properties/configTest.properties";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    static String dir = "testFiles/json/";
    static PropertiesStorage propertiesStorage;

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @Before
    public void setUp() throws Exception {
        random = new SecureRandom();

        propertiesStorage = PropertiesStorage.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        Date date = new Date();
        String dateString = formatter.format(date);

        File file = new File(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        file.delete();
        File fileConfig = new File(testConfigString);
        fileConfig.delete();
    }

    @Test
    public void receiveTransactionJSON() throws Throwable {
        Date date = new Date();
        String dateString = formatter.format(date);

        Files.copy(Paths.get(configString), Paths.get(testConfigString),
                StandardCopyOption.REPLACE_EXISTING);

        propertiesStorage.setPropertiesPath(testConfigString);

        JSONHistoryKeeper.setDir(dir);
        JSONHistoryKeeper jsonHistoryKeeper = new JSONHistoryKeeper();

        CurrencyUnitJSONStorageFactory factory = new CurrencyUnitJSONStorageFactory();
        CurrencyUnitJSONStorage currencyUnitJSONStorage = (CurrencyUnitJSONStorage) factory.createStorage();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        Card card = new Card(USDUnit, jsonHistoryKeeper);

        Money beforeBalance = card.getBalance();
        Money money = Money.of(USDUnit,randomValue());

        Transaction addTransaction = new AddTransaction(money);

        card.receiveTransaction(addTransaction);
        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),money.getAmount());

        jsonHistoryKeeper.close();

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.transactionAmountJSONName),money.toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.beforeBalanceJSONName),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.afterBalanceJSONName),balance.toString());
    }
}