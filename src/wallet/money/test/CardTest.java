package wallet.money.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wallet.PropertiesStorage;
import wallet.card.*;
import wallet.money.*;
import wallet.money.Money;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUnitJSONStorage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class CardTest {

    static SecureRandom random;
    static PropertiesStorage propertiesStorage;

    String jsonFilePath = "testFiles/json/cardHistory.json";
    String id = "7658123";

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @BeforeClass
    public static void setUp() throws IOException {
        propertiesStorage = PropertiesStorage.getInstance();
        propertiesStorage.setPropertiesPath("testFiles/properties/config.properties");
        random = new SecureRandom();
    }

    @Test
    public void receiveAddTransaction() throws IOException, ParseException {
        HistoryKeeper historyKeeper = new JSONHistoryKeeper(jsonFilePath);
        CurrencyUnitJSONStorage currencyUnitJSONStorage = CurrencyUnitJSONStorage.getInstance();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        Card card = new Card(historyKeeper,USDUnit,id);

        Money beforeBalance = card.getBalance();
        Money money = Money.of(USDUnit,randomValue());

        Transaction addTransaction = new AddTransaction(money);

        card.receiveTransaction(addTransaction);
        historyKeeper.saveState();

        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),money.getAmount());

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get("transactionAmount"),money.toString());
        Assert.assertEquals(snapshotObject.get("beforeBalance"),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get("afterBalance"),balance.toString());

        File file = new File(jsonFilePath);
        file.delete();
    }

    @Test
    public void receiveReduceTransaction() throws IOException, ParseException {
        HistoryKeeper historyKeeper = new JSONHistoryKeeper(jsonFilePath);
        CurrencyUnitJSONStorage currencyUnitJSONStorage = CurrencyUnitJSONStorage.getInstance();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        Money beforeBalance = Money.of(USDUnit,BigDecimal.valueOf(10000));
        Card card = new Card(historyKeeper,USDUnit,beforeBalance,id);

        Money money = Money.of(USDUnit,randomValue());

        Transaction reduceTransaction = new ReduceTransaction(money);

        card.receiveTransaction(reduceTransaction);
        historyKeeper.saveState();

        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),beforeBalance.minus(money).getAmount());

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get("transactionAmount"),money.toString());
        Assert.assertEquals(snapshotObject.get("beforeBalance"),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get("afterBalance"),balance.toString());

        File file = new File(jsonFilePath);
        file.delete();
    }

    @Test
    public void receiveAddTransactionAnotherCurrency() throws IOException, ParseException {
        HistoryKeeper historyKeeper = new JSONHistoryKeeper(jsonFilePath);
        CurrencyUnitJSONStorage currencyUnitJSONStorage = CurrencyUnitJSONStorage.getInstance();
        CurrencyConverter currencyConverter = CurrencyConverter.getInstance();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        StrictCurrencyUnit EURUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("EUR");
        Card card = new Card(historyKeeper,USDUnit,id);

        Money beforeBalance = card.getBalance();
        Money money = Money.of(EURUnit,randomValue());

        Transaction addTransaction = new AddTransaction(money);

        card.receiveTransaction(addTransaction);
        historyKeeper.saveState();

        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(), currencyConverter.convert(money,USDUnit).getAmount());

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get("transactionAmount"),currencyConverter.convert(money,USDUnit).toString());
        Assert.assertEquals(snapshotObject.get("notConvertedTransactionAmount"),money.toString());
        Assert.assertEquals(snapshotObject.get("beforeBalance"),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get("afterBalance"),balance.toString());

        File file = new File(jsonFilePath);
        file.delete();
    }

}