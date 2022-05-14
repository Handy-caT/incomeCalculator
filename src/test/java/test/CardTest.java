package test;

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
import wallet.card.transaction.ReduceTransaction;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUnitJSONStorage;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUnitJSONStorageFactory;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;

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
import java.util.LinkedList;
import java.util.List;

public class CardTest {

    static SecureRandom random;
    static final String configString = "testFiles/properties/cardConfig.properties";
    static final String testConfigString = "testFiles/properties/cardConfigTest.properties";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    static final String dir = "testFiles/json/";
    static final  String jsonPath = "testFiles/json/testapi.json";
    static PropertiesStorage propertiesStorage;

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @Before
    public void setUp() throws IOException {
        random = new SecureRandom();

        TestAPI testAPI = new TestAPI(jsonPath);
        CurrencyUpdaterWeb.setApi(testAPI);

        propertiesStorage = PropertiesStorage.getInstance();


        Files.copy(Paths.get(configString), Paths.get(testConfigString),
                StandardCopyOption.REPLACE_EXISTING);

        propertiesStorage.setPropertiesPath(testConfigString);

        JSONHistoryKeeper.setDir(dir);
    }

    @After
    public void tearDown() {
        Date date = new Date();
        String dateString = formatter.format(date);

        File file = new File(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        file.delete();
        File fileConfig = new File(testConfigString);
        fileConfig.delete();
    }

    private void checkTransactions(Money beforeBalance, Money money, Money balance) throws IOException, ParseException {
        Date date = new Date();
        String dateString = formatter.format(date);

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.transactionAmountJSONName),money.toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.beforeBalanceJSONName),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.afterBalanceJSONName),balance.toString());
    }

    @Test
    public void receiveTransactionJSON() throws Throwable {

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

        checkTransactions(beforeBalance, money, balance);
    }

    @Test
    public void receiveSubtractTransactionJSON() throws IOException, ParseException {

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

        Transaction subtractTransaction = new ReduceTransaction(money);

        card.receiveTransaction(subtractTransaction);
        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),money.getAmount().negate());

        jsonHistoryKeeper.close();

        checkTransactions( beforeBalance, money, balance);
    }

    @Test
    public void receiveOtherCurrencyTransactionJSON() throws IOException, ParseException {
        JSONHistoryKeeper jsonHistoryKeeper = new JSONHistoryKeeper();

        CurrencyUnitJSONStorageFactory factory = new CurrencyUnitJSONStorageFactory();
        CurrencyUnitJSONStorage currencyUnitJSONStorage = (CurrencyUnitJSONStorage) factory.createStorage();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        StrictCurrencyUnit EURUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("EUR");
        Card card = new Card(USDUnit, jsonHistoryKeeper);

        Money beforeBalance = card.getBalance();
        Money money = Money.of(EURUnit,randomValue());

        Transaction addTransaction = new AddTransaction(money);

        CurrencyConverter converter = CurrencyConverter.getInstance();

        card.receiveTransaction(addTransaction);
        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),converter.convert(money,USDUnit).getAmount());

        jsonHistoryKeeper.close();

        checkTransactions(beforeBalance, money, balance);
    }

    @Test
    public void restoreTransactionTest() throws IOException, ParseException {
        Date date = new Date();
        String dateString = formatter.format(date);

        JSONHistoryKeeper jsonHistoryKeeper = new JSONHistoryKeeper();

        CurrencyUnitJSONStorageFactory factory = new CurrencyUnitJSONStorageFactory();
        CurrencyUnitJSONStorage currencyUnitJSONStorage = (CurrencyUnitJSONStorage) factory.createStorage();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        Card card = new Card(USDUnit, jsonHistoryKeeper);

        int size = 15;
        List<Transaction> transactionList = new LinkedList<>();
        for(int i = 0; i < size; ++i) {
            Money money = Money.of(USDUnit,randomValue());
            Transaction addTransaction = new AddTransaction(money);

            transactionList.add(addTransaction);
        }

        Money sum = Money.of(USDUnit,BigDecimal.ZERO);

        List<Money> beforeBalanceList = new LinkedList<>();
        List<Money> afterBalanceList = new LinkedList<>();

        for (Transaction addTransaction : transactionList) {
            beforeBalanceList.add(card.getBalance());

            card.receiveTransaction(addTransaction);
            sum = sum.plus(addTransaction.getTransactionAmount());

            afterBalanceList.add(card.getBalance());
        }

        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),sum.getAmount());

        jsonHistoryKeeper.close();

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        for(int i = 0; i < size; ++i) {
            JSONObject snapshotObject = (JSONObject) array.get(i);
            Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.transactionAmountJSONName),
                    transactionList.get(i).getTransactionAmount().toString());
            Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.beforeBalanceJSONName),
                    beforeBalanceList.get(i).toString());
            Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.afterBalanceJSONName),
                    afterBalanceList.get(i).toString());
        }

        int indexToRestore = random.nextInt(size);
        Transaction transactionToRestore = transactionList.get(indexToRestore);
        jsonHistoryKeeper.restoreTransaction(card,transactionToRestore);

        jsonHistoryKeeper.close();

        jsonParser = new JSONParser();

        fileReader = new FileReader(dir + JSONHistoryKeeper.defaultFileName + dateString + ".json");
        array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(size);
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.transactionAmountJSONName),
                transactionToRestore.getTransactionAmount().toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.beforeBalanceJSONName),
                afterBalanceList.get(size - 1).toString());
        Assert.assertEquals(snapshotObject.get(JSONHistoryKeeper.afterBalanceJSONName),
                afterBalanceList.get(size - 1)
                        .minus(transactionToRestore.getTransactionAmount()).toString());
    }


}